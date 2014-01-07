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

package cl.niclabs.cb.tscrypto;

import cl.niclabs.tscrypto.common.utils.TSLogger;

import cl.niclabs.tscrypto.sigDealer.RequestManager;
import cl.niclabs.tscrypto.sigDealer.SDConfig;
import cl.niclabs.cb.dispatcher.MethodDispatcher;
import cl.niclabs.cb.tscrypto.methods.TsCryptoMethodFactory;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MethodCollector extends DefaultConsumer {

    private ExecutorService executor;
    private TsCryptoMethodFactory methodFactory;

    public MethodCollector(Connection connection, RequestManager requestManager) throws IOException {
        super(connection.createChannel());

        methodFactory = new TsCryptoMethodFactory(requestManager);

        String queue = SDConfig.getInstance().getRpcQueue();

        // TODO: Put this on config file
        executor = Executors.newScheduledThreadPool(1);

        getChannel().queueDeclare(queue, false, false, false, null);
        getChannel().basicQos(1);

        boolean autoAck = false;
        getChannel().basicConsume(queue, autoAck, this);
    }

    @Override
    public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
        try {
            getChannel().close();
            getChannel().getConnection().close();
            methodFactory.close();
            executor.shutdown();
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        executor.execute(new Handle(body, envelope, properties));
    }

    private class Handle implements Runnable {

        byte[] bytes;
        Envelope envelope;
        AMQP.BasicProperties properties;
        AMQP.BasicProperties replyProperties;

        private Handle(byte[] bytes, Envelope envelope, AMQP.BasicProperties properties) {
            this.bytes = bytes;
            this.envelope = envelope;
            this.properties = properties;
            this.replyProperties = new AMQP.BasicProperties()
                    .builder()
                    .correlationId(properties.getCorrelationId())
                    .build();
        }

        @Override
        public void run() {
            String message = new String(bytes);
            TSLogger.methodCollector.debug("Received message: " + message);
            String response = MethodDispatcher.dispatch(message, methodFactory);

            try {
                TSLogger.methodCollector.debug("Sending " + response + "...");
                getChannel().basicPublish("", properties.getReplyTo(), replyProperties, response.getBytes());
                getChannel().basicAck(envelope.getDeliveryTag(), false);
            }
            catch (Exception e) {
                TSLogger.methodCollector.error("Cannot send message to server...", e);
            }

        }
    }
}
