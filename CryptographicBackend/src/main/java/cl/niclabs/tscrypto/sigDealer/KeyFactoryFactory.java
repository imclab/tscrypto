package cl.niclabs.tscrypto.sigDealer;

import cl.niclabs.tscrypto.common.algorithms.keyfactory.KeyFactory;
import cl.niclabs.tscrypto.common.algorithms.keyfactory.KeyFactoryImpl;
import cl.niclabs.tscrypto.common.algorithms.keyfactory.KeyFactoryJniImpl;

public class KeyFactoryFactory {
    public static KeyFactory getInstance() {
        if (SDConfig.getInstance().getUseJni()) {
            return new KeyFactoryJniImpl();
        } else {
            return new KeyFactoryImpl();
        }
    }
}
