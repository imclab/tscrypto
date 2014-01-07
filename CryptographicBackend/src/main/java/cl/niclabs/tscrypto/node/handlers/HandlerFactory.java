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

package cl.niclabs.tscrypto.node.handlers;

import java.util.HashMap;

import cl.niclabs.tscrypto.common.messages.DeleteKeyQuery;
import cl.niclabs.tscrypto.common.messages.PingQuery;
import cl.niclabs.tscrypto.common.messages.SignShareQuery;
import cl.niclabs.tscrypto.common.messages.TSMessage;

/**
 * Creates a Handler given a TSMessage
 * @author acansado
 *
 */
// TODO repeated class
public class HandlerFactory {

	private static HashMap<Class<?>, Class<?>> factories = new HashMap<>();

	static {
		addHandler(SignShareQuery.class, SignHandler.class);
		addHandler(DeleteKeyQuery.class, DeleteKeyHandler.class);
	}

	private static void addHandler(Class<?> messageClass, Class<?> handlerClass) {
		factories.put(messageClass, handlerClass);
	}

	public static NodeHandler createMessageHandler(TSMessage message) throws ClassNotFoundException, InstantiationException, IllegalAccessException {

		Class<?> handlerClass = factories.get(message.getClass());

		NodeHandler handler = (NodeHandler) handlerClass.newInstance();
		handler.init(message);

		return handler;
	}
}
