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

import java.io.Closeable;
import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import cl.niclabs.tscrypto.common.messages.TSMessage;

public class Dispatcher implements Closeable {

	private Channel channel;

	public Dispatcher(Connection connection) throws IOException {
		channel = connection.createChannel();
	}

	public void send(TSMessage message, String replyTo) throws IOException {

		channel.basicPublish(
    			"",			// exchange
    			replyTo,	// queue name
    			null,		// other properties for the message - routing headers etc
    			message.toJsonPretty().getBytes()
    			);
	}

    @Override
    public void close() throws IOException {
        channel.close();
    }
}
