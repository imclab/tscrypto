/*
    Copyright 2013 NIC Chile Research Labs
    This file is part of TsCrypto.

    TsCrypto is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    TsCrypto is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with TsCrypto.  If not, see <http://www.gnu.org/licenses/>.
 */

package cl.niclabs.tscrypto.sigDealer;

import cl.niclabs.tscrypto.common.config.RabbitMQConfig;
import cl.niclabs.tscrypto.common.datatypes.KeyInfo;
import cl.niclabs.tscrypto.common.datatypes.KeyShareInfo;
import cl.niclabs.tscrypto.common.datatypes.Ticket;
import cl.niclabs.tscrypto.common.messages.DeleteKeyQuery;
import cl.niclabs.tscrypto.common.messages.SendKeyQuery;
import cl.niclabs.tscrypto.common.messages.TSMessage;
import cl.niclabs.tscrypto.common.utils.TSLogger;
import cl.niclabs.tscrypto.common.utils.Util;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.Closeable;
import java.io.IOException;


// NOTE: This class needs to disappear.
public class KeyDispatcher implements Closeable {

    // Dispatch to each node the information that they want to have...
    private final Channel channel;
    private final RabbitMQConfig config;

    public KeyDispatcher(Connection connection, RabbitMQConfig config) throws IOException {
        this.channel = connection.createChannel();
        this.config = config;
    }

    private void dispatch(TSMessage[] messages) throws IOException {
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
                    config.getClientQueue()
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
