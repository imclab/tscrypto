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

import java.io.Closeable;
import java.io.IOException;

import cl.niclabs.tscrypto.common.config.RabbitMQConfig;
import cl.niclabs.tscrypto.common.messages.TSMessage;

import cl.niclabs.tscrypto.common.utils.TSLogger;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class Dispatcher implements Closeable {

	private Channel channel;
	private RabbitMQConfig rconfig;

	public Dispatcher(Connection connection, RabbitMQConfig rconfig) throws IOException {
		this.rconfig = rconfig;
		channel = connection.createChannel();
	}
	
	public void send(String alias, TSMessage message) throws IOException {
        TSLogger.sd.debug(message.toJsonPretty());

		channel.basicPublish(
				rconfig.getRequestsExchange(alias),
    			"", 	// routing key
    			null,	// other properties for the message - routing headers etc
    			message.toJsonPretty().getBytes()
    			);

	}

    @Override
    public void close() throws IOException {
        channel.close();
    }
}
