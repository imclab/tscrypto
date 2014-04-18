package cl.niclabs.cb.common;
import cl.niclabs.cb.common.methods.MethodFactory;
import cl.niclabs.tscrypto.common.utils.TSLogger;
import cl.niclabs.tscrypto.manager.SDConfig;
import org.zeromq.ZMQ;


public class MethodCollector {
    private ZMQ.Socket socket;
    private MethodFactory methodFactory;
    private Thread thread;
    private boolean running;

    public MethodCollector(ZMQ.Context context, MethodFactory methodFactory) {
        SDConfig config = SDConfig.getInstance();
        this.methodFactory = methodFactory;
        thread = null;

        socket = context.socket(ZMQ.REP);
        socket.bind("tcp://*:" + config.getMethodCollectorPort());

        running = false;
    }

    public void start() {
        if (!running) {
            running = true;
            thread = new CollectorThread();
            thread.start();
        }
    }

    public void stop() throws InterruptedException {
        if(running) {
            running = false;
            thread.join();
        }
    }

    private class CollectorThread extends Thread {
        @Override
        public void run() {
            while (running) {
                String message = socket.recvStr();
                TSLogger.methodCollector.debug("Received: " + message);
                String response = MethodDispatcher.dispatch(message, methodFactory);
                TSLogger.methodCollector.debug("Sent: " + response);
                socket.send(response.getBytes());
            }
        }
    }

}
