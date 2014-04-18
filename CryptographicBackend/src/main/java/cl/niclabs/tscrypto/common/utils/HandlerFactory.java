package cl.niclabs.tscrypto.common.utils;
import cl.niclabs.tscrypto.common.messages.TSMessage;

import java.util.HashMap;

public class HandlerFactory<T extends Handler> {
    private final HashMap<Class<?>, Class<?>> factories = new HashMap<>();

    public void addHandler(Class<?> messageClass, Class<?> handlerClass) {
        factories.put(messageClass, handlerClass);
    }

    public T createMessageHandler(TSMessage message) throws ClassNotFoundException, InstantiationException, IllegalAccessException {

        Class<?> handlerClass = factories.get(message.getClass());

        if(handlerClass == null) {
            throw new ClassNotFoundException(message.getClass().getCanonicalName());
        }

        T handler = (T) handlerClass.newInstance();

        handler.init(message);

        return handler;
    }
}
