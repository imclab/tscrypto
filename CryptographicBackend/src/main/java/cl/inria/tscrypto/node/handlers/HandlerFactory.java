package cl.inria.tscrypto.node.handlers;

import java.util.HashMap;

import cl.inria.tscrypto.common.messages.PingQuery;
import cl.inria.tscrypto.common.messages.SignShareQuery;
import cl.inria.tscrypto.common.messages.TSMessage;

/**
 * Creates a Handler given a TSMessage
 * @author acansado
 *
 */
// TODO repeated class
public class HandlerFactory {

	private static HashMap<Class<?>, Class<?>> factories = new HashMap<Class<?>, Class<?>>();

	static {
		addHandler(SignShareQuery.class, SignHandler.class);
		addHandler(PingQuery.class,	PingHandler.class);
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
