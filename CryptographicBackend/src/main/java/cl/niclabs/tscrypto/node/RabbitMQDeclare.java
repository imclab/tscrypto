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

import java.io.IOException;

import cl.niclabs.tscrypto.common.config.RabbitMQConfig;

import com.rabbitmq.client.Channel;

public class RabbitMQDeclare {

	public static void declareAndBindQueues(Channel channel, RabbitMQConfig rconfig, int id, String keyAlias) throws IOException {
		channel.queueDeclare(
				rconfig.getRequestsQueue(id),			// queue name
				rconfig.getQueueDeclareDurable(),
				rconfig.getQueueDeclareExclusive(),
				rconfig.getQueueDeclareAutodelete(),
				null	// arguments
				);
		
		channel.queueBind(
				rconfig.getRequestsQueue(id),	// queue name
				rconfig.getRequestsExchange(keyAlias),
				""	// routing key
				);
	}

	public static void declareExchanges(Channel channel, RabbitMQConfig rconfig, String keyAlias) throws IOException {
		channel.exchangeDeclare(
				rconfig.getRequestsExchange(keyAlias),	// exchange
				"fanout",	// type
				rconfig.getExchangeDeclareDurable(),
				rconfig.getExchangeDeclareAutodelete(),
				rconfig.getExchangeDeclareInternal(),
				null	// arguments
				);

	}

}
