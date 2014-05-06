package cl.niclabs.tscrypto.node.internodeCommunication;

import cl.niclabs.tscrypto.common.messages.TSMessage;
import cl.niclabs.tscrypto.node.Dispatcher;
import cl.niclabs.tscrypto.node.NodeConfig;
import org.zeromq.ZMQ;

import java.io.Closeable;
import java.io.IOException;

public class InternodeDispatcher implements Closeable {
    private NodeConfig config;
    private ZMQ.Socket socket;

    public InternodeDispatcher(ZMQ.Context context) {
        config = NodeConfig.getInstance();
        socket = context.socket(ZMQ.PUSH);
        socket.connect("tcp://" + config.getManagerAddress() + ":" + config.getOutgoingRoutingPort());
    }

    public void send(TSMessage message, int node) throws IOException {
        socket.sendMore(config.getIdentity(node));
        socket.send(message.toJson().getBytes());
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }
}
