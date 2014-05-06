package cl.niclabs.tscrypto.manager;

import cl.niclabs.tscrypto.common.datatypes.Collector;
import cl.niclabs.tscrypto.common.utils.TSLogger;
import org.zeromq.ZFrame;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import java.io.Closeable;
import java.io.IOException;

public class RouterCollector implements Collector, Closeable {
    final private ZMQ.Socket frontend;
    final private ZMQ.Socket backend;

    private boolean running;
    private Thread thread;

    public RouterCollector(ZMQ.Context context) {
        SDConfig config = SDConfig.getInstance();
        frontend = context.socket(ZMQ.ROUTER);
        backend = context.socket(ZMQ.ROUTER);
        frontend.bind("tcp://*:" + config.getIncomingRoutingPort());    // Where REQs gets connected
        backend.bind("tcp://*:" + config.getOutgoingRoutingPort()); // Where REPs gets connected
    }

    @Override
    public void start() {
        if(!running) {
            running = true;
            thread = new CollectorThread();
            thread.start();
        }
    }

    @Override
    public void stop() throws InterruptedException {
        if(running) {
            running = false;
            thread.join();
        }
    }

    @Override
    public void close() throws IOException {
        frontend.close();
        backend.close();
    }

    private class CollectorThread extends Thread {
        @Override
        public void run() {
            ZMQ.Poller poller = new ZMQ.Poller(2); // 2 sockets.
            poller.register(frontend, ZMQ.Poller.POLLIN);
            poller.register(backend, ZMQ.Poller.POLLIN);
            while(running) {
                poller.poll();
                if(poller.pollin(0)) { // Frontend
                    ZMsg msg = ZMsg.recvMsg(frontend);
                    ZFrame reqIdentity = msg.unwrap();
                    ZFrame repIdentity = msg.pop();
                    ZFrame realMsg = msg.pop();

                    TSLogger.sd.debug("FROM: " + reqIdentity);
                    TSLogger.sd.debug("TO: " + repIdentity);

                    backend.sendMore(repIdentity.getData());
                    backend.sendMore("");
                    backend.sendMore(reqIdentity.getData());
                    backend.send(realMsg.getData());
                }
                if(poller.pollin(1)) { // Backend
                    ZMsg msg = ZMsg.recvMsg(backend);
                    ZFrame repIdentity = msg.unwrap();
                    ZFrame reqIdentity = msg.pop();
                    ZFrame realMsg = msg.pop();

                    TSLogger.sd.debug("FROM: " + repIdentity);
                    TSLogger.sd.debug("TO: " + reqIdentity);

                    frontend.sendMore(reqIdentity.getData());
                    frontend.sendMore("");
                    frontend.sendMore(repIdentity.getData());
                    frontend.send(realMsg.getData());
                }
            }
        }
    }

    public static void main(String...args) throws InterruptedException, IOException {
        try(
            ZMQ.Context context = ZMQ.context(1);
            RouterCollector routerCollector = new RouterCollector(context);
        ) {
            routerCollector.start();
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (RouterCollector.class) {
                    System.out.println("Routing messages!");
                    RouterCollector.class.wait();
                }
            }
            routerCollector.stop();
        }
    }
}
