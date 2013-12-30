package cl.inria.tscrypto.keyFactory;

import cl.inria.tscrypto.common.config.RabbitMQConfig;
import cl.inria.tscrypto.common.datatypes.KeyInfo;
import cl.inria.tscrypto.common.datatypes.KeyShareInfo;
import cl.inria.tscrypto.common.datatypes.Ticket;
import cl.inria.tscrypto.common.messages.DeleteKeyQuery;
import cl.inria.tscrypto.common.messages.MessageAsync;
import cl.inria.tscrypto.common.messages.SendKeyQuery;
import cl.inria.tscrypto.common.messages.TSMessage;
import cl.inria.tscrypto.common.utils.TSLogger;
import cl.inria.tscrypto.common.utils.Util;
import cl.inria.tscrypto.sigDealer.SDConfig;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.Closeable;
import java.io.IOException;
import java.math.BigInteger;

public class KeyDispatcher implements Closeable {

    // Dispatch to each node the information that they want to have...
    private final Channel channel;
    private final RabbitMQConfig config;

    public KeyDispatcher(Connection connection, RabbitMQConfig config) throws IOException {
        this.channel = connection.createChannel();
        this.config = config;
    }

    public void dispatch(TSMessage[] messages) throws IOException {
        for(int i=0; i<messages.length; i++) {
            String queue = Util.keyQueueName(config, i);
            String message = messages[i].toJson();

            TSLogger.keyFactory.debug("Sending message: " + message);
            channel.queueDeclare(queue, false, false, false, null);
            channel.basicPublish("", queue, null, message.getBytes());
        }
    }


    public void dispatch(KeyInfo keyInfo, Ticket ticket) throws IOException {
        String label = keyInfo.getKeyMetaInfo().getAlias();
        TSMessage[] messages = new TSMessage[keyInfo.getKeyMetaInfo().getL()];
        for(int i=0; i<messages.length; i++) {
            KeyShareInfo keyShareInfo = new KeyShareInfo(
                    keyInfo.getKeyMetaInfo(),
                    keyInfo.getPublicKey(),
                    keyInfo.getKeyShares().getSecret(i)
            );

            SendKeyQuery query = new SendKeyQuery(
                    ticket,
                    label,
                    keyShareInfo,
                    config.getKeyManagementQueue()
            );

            messages[i] = query;
        }

        dispatch(messages);
    }

    public void dispatchDeleteKey(String label, Ticket ticket) throws IOException {
        int l = SDConfig.getInstance().getL();

        DeleteKeyQuery query = new DeleteKeyQuery(label, ticket, config.getKeyManagementQueue());
        TSMessage[] messages = new TSMessage[l];
        for (int i=0; i<l; i++) {
            messages[i] = query;
        }
    }

    @Override
    public void close() throws IOException {
        channel.close();
    }
}
