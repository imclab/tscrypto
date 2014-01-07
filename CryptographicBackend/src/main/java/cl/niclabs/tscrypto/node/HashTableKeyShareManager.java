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

import java.util.Hashtable;

import cl.niclabs.tscrypto.common.datatypes.KeyShareInfo;
import cl.niclabs.tscrypto.common.datatypes.TSPublicKey;

public class HashTableKeyShareManager implements KeyShareManager {

    private Hashtable<String, KeyShareInfo> keys = new Hashtable<>();

    public HashTableKeyShareManager() {}

    @Override
    public void addKey(String label, KeyShareInfo key) {
		keys.put(label, key);
	}
	
	@Override
    public void removeKey(String label) {
		if (keys.contains(label)) {
            keys.remove(label);
        }
	}

	@Override
    public TSPublicKey getPublicKey(String label) {
		return keys.get(label).getPublicKey();
	}

    @Override
    public KeyShareInfo getKeyShareInfo(String label) {
        return keys.get(label);
    }
}
