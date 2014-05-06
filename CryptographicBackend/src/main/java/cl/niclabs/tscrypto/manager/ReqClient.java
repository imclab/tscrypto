package cl.niclabs.tscrypto.manager;

import org.zeromq.ZMQ;

public class ReqClient {
    public static void main(String...args) {
        try (
                ZMQ.Context context = ZMQ.context(1);
                ZMQ.Socket socket = context.socket(ZMQ.REQ);
        ){
            socket.connect("tcp://localhost:11000");
            byte[] key = "node-1".getBytes();
            socket.sendMore(key);
            socket.send("Hola!");

            String repKey = socket.recvStr();
            String data = socket.recvStr();

            System.out.println(repKey + " : " + data);
        }

    }
}
