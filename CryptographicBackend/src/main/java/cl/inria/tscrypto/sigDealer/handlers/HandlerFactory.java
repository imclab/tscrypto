package cl.inria.tscrypto.sigDealer.handlers;

import java.util.HashMap;

import cl.inria.tscrypto.common.messages.SendKeyAnswer;
import cl.inria.tscrypto.common.messages.SignShareAnswer;
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
		addHandler(SignShareAnswer.class, SignedShareHandler.class);
	}

	private static void addHandler(Class<?> messageClass, Class<?> handlerClass) {
		factories.put(messageClass, handlerClass);
	}

	public static SDHandler createMessageHandler(TSMessage message) throws ClassNotFoundException, InstantiationException, IllegalAccessException {

		Class<?> handlerClass = factories.get(message.getClass());

		SDHandler handler = (SDHandler) handlerClass.newInstance();
		handler.init(message);

		return handler;
	}
}
