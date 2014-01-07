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

package cl.niclabs.tscrypto.node;

import cl.niclabs.tscrypto.common.messages.SendKeyAnswer;
import cl.niclabs.tscrypto.common.messages.SendKeyQuery;
import cl.niclabs.tscrypto.common.messages.TSMessage;
import cl.niclabs.tscrypto.common.utils.TSLogger;
import cl.niclabs.tscrypto.common.utils.Util;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KeyCollector extends DefaultConsumer {

    private ExecutorService executor;
    private KeyShareManager manager;
    private Dispatcher dispatcher;

    public KeyCollector(Connection connection, Dispatcher dispatcher, KeyShareManager manager) throws IOException {
        super(connection.createChannel());

        NodeConfig config = NodeConfig.getInstance();
        this.dispatcher = dispatcher;
        this.manager = manager;
        this.executor = Executors.newScheduledThreadPool(config.getNumThreads());

        // The name of the queue has the id...
        String queue = Util.keyQueueName(config.getRabbitMQConfig(), config.getNodeId());
        boolean autoAck = true;
        Consumer consumer = this;

        getChannel().queueDeclare(queue, false, false, false, null);
        getChannel().basicConsume(queue, autoAck, consumer);

    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        TSLogger.node.info("KeyCollector: new SendKeyQuery received.");
        TSLogger.node.debug(new String(body));
        executor.execute(new Handler(body));
    }

    @Override
    public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
        try {
            getChannel().close();
            getChannel().getConnection().close();
            executor.shutdown();
        } catch (IOException e) {
            TSLogger.node.error("Error when shuttingdown KeyCollector " + e.getMessage(), e);
        }
    }

    private class Handler implements Runnable {
        byte[] body;

        private Handler(byte[] body) {
            this.body = body;
        }

        @Override
        public void run() {
            NodeConfig config = NodeConfig.getInstance();

            String jsonText = new String(body);
            SendKeyQuery message = TSMessage.fromJson(jsonText);
            manager.addKey(message.getLabel(), message.getKeyInfo());
            TSLogger.node.info("Key " + message.getLabel() + " added to manager.");

            SendKeyAnswer response = new SendKeyAnswer(
                    message.ticket,
                    config.getNodeId(),
                    message.getReplyTo()
            );

            try {
                dispatcher.send(response, message.getReplyTo());
            } catch (IOException e) {
                TSLogger.node.error("Cannot send ACK", e);
            }
        }
    }


}
