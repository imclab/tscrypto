package cl.inria.tscrypto.keyFactory;

import cl.inria.tscrypto.common.config.RabbitMQConfig;
import cl.inria.tscrypto.common.datatypes.KeyInfo;
import cl.inria.tscrypto.common.datatypes.KeyShareInfo;
import cl.inria.tscrypto.common.datatypes.Ticket;
import cl.inria.tscrypto.common.messages.SendKeyQuery;
import cl.inria.tscrypto.common.utils.TSLogger;
import cl.inria.tscrypto.common.utils.Util;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.Closeable;
import java.io.IOException;
import java.math.BigInteger;

public class KeyDispatcher implements Closeable {

    private final Channel channel;
    private final RabbitMQConfig config;

    public KeyDispatcher(Connection connection, RabbitMQConfig config) throws IOException {
        this.channel = connection.createChannel();
        this.config = config;
    }

    public void dispatch(KeyInfo keyInfo, Ticket ticket) throws IOException {
        String alias = keyInfo.getKeyMetaInfo().getAlias();

        for(int i=0; i<keyInfo.getKeyMetaInfo().getL(); i++) {
            String queue = Util.keyQueueName(config, i);

            KeyShareInfo keyShareInfo = new KeyShareInfo(
                    keyInfo.getKeyMetaInfo(),
                    keyInfo.getPublicKey(),
                    keyInfo.getKeyShares().getSecret(i)
            );

            SendKeyQuery query = new SendKeyQuery(
                    ticket,
                    alias,
                    keyShareInfo,
                    config.getKeyManagementQueue()
            );

            String message = query.toJson();

            TSLogger.keyFactory.debug("Sending keyShareInfo: " + message);

            channel.queueDeclare(queue, false, false, false, null);
            channel.basicPublish("", queue, null, message.getBytes());
        }
    }

    @Override
    public void close() throws IOException {
        channel.close();
    }
}
