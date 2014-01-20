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

import cl.niclabs.tscrypto.common.datatypes.KeyInfo;
import cl.niclabs.tscrypto.common.utils.TSLogger;

public class KeyDispatchRequest implements Request {
    private final boolean[] dispatchedKeys;

    public KeyDispatchRequest(KeyInfo keyInfo) {
        int l = keyInfo.getKeyMetaInfo().getL();
        this.dispatchedKeys = new boolean[l];
        for(int i=0; i<l; i++) {
            dispatchedKeys[i] = false;
        }
    }

    @Override
    public synchronized void setReady(int id) {
        dispatchedKeys[id] = true;
        notifyAll();
    }

    @Override
    public boolean isReady() {
        for(boolean ready: dispatchedKeys) {
            if(!ready)
                return false;
        }
        return true;
    }

    @Override
    public void waitUntilReady() throws InterruptedException {
        while (!isReady()) {
            synchronized (this) {
                while (!isReady()) {
                    TSLogger.keyFactory.info("Waiting nodes to notify they added the key...");
                    wait(2000);
                }
            }
        }
    }


}
