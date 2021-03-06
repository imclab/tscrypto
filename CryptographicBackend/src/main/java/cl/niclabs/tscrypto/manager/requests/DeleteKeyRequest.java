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

package cl.niclabs.tscrypto.manager.requests;

import cl.niclabs.tscrypto.common.utils.TSLogger;
import cl.niclabs.tscrypto.manager.Request;
import cl.niclabs.tscrypto.manager.SDConfig;

public class DeleteKeyRequest extends Request {
    private final boolean[] deletedKeys;

    public DeleteKeyRequest() {
        int l = SDConfig.getInstance().getL();
        deletedKeys = new boolean[l];
        for(int i=0; i<l; i++) {
            deletedKeys[i] = false;
        }
    }

    @Override
    public synchronized void setReady(int id) {
        deletedKeys[id] = true;
        notifyAll();
    }

    public boolean isReady() {
        for(boolean ready: deletedKeys) {
            if(!ready)
                return false;
        }
        return true;
    }

}
