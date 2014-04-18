package cl.niclabs.tscrypto.node;

import cl.niclabs.tscrypto.common.messages.TSMessage;
import org.zeromq.ZMQ;

import javax.xml.soap.Node;
import java.io.Closeable;
import java.io.IOException;

public class DispatcherZero implements Dispatcher, Closeable{
    private ZMQ.Socket socket;

    public DispatcherZero(ZMQ.Context context) {
        NodeConfig config = NodeConfig.getInstance();
        socket = context.socket(ZMQ.PUSH);
        socket.connect("tcp://" + config.getManagerAddress() + ":" + config.getResultsPort());
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

    @Override
    public void send(TSMessage message, String replyTo) throws IOException {
        socket.send(message.toJson().getBytes());
    }
}
