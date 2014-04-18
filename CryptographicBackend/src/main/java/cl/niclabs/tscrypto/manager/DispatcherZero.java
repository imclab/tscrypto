package cl.niclabs.tscrypto.manager;

import cl.niclabs.tscrypto.common.datatypes.EncryptedData;
import cl.niclabs.tscrypto.common.datatypes.JsonFormat;
import cl.niclabs.tscrypto.common.messages.TSMessage;
import org.zeromq.ZMQ;

import java.io.Closeable;
import java.io.IOException;

public class DispatcherZero implements Dispatcher, Closeable{
    private ZMQ.Socket socket;

    public DispatcherZero(ZMQ.Context context) {
        SDConfig config = SDConfig.getInstance();
        socket = context.socket(ZMQ.PUB);
        socket.bind("tcp://*:" + config.getDispatcherPort());
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

    @Override
    public void dispatch(String envelope, JsonFormat message) throws IOException {
        socket.sendMore(envelope.getBytes());
        socket.send(message.toJson().getBytes());
    }
}
