package cl.niclabs.tscrypto.sigDealer;

import cl.niclabs.tscrypto.common.algorithms.KeyFactory;
import cl.niclabs.tscrypto.common.algorithms.KeyFactoryImpl;
import cl.niclabs.tscrypto.common.algorithms.KeyFactoryJniImpl;

public class KeyFactoryFactory {
    public static KeyFactory getInstance() {
        if (SDConfig.getInstance().getUseJni()) {
            return new KeyFactoryJniImpl();
        } else {
            return new KeyFactoryImpl();
        }
    }
}
