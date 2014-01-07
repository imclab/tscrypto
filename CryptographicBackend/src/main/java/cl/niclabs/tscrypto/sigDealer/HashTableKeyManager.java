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
import cl.niclabs.tscrypto.common.datatypes.KeyMetaInfo;
import cl.niclabs.tscrypto.common.datatypes.TSPublicKey;

import java.util.Hashtable;
import java.util.Map;

public class HashTableKeyManager implements KeyManager { //TODO: check if it's thread safe...

    static public class Pair {
        TSPublicKey publicKey;
        KeyMetaInfo keyMetaInfo;
    }

    private Map<String, Pair> map = new Hashtable<>();

    @Override
    public void addKey(KeyInfo key) {
        String label = key.getKeyMetaInfo().getAlias();
        Pair pair = new Pair();
        pair.publicKey = key.getPublicKey();
        pair.keyMetaInfo = key.getKeyMetaInfo();
        map.put(label, pair);
    }

    @Override
    public void removeKey(String label) {
        map.remove(label);
    }

    @Override
    public boolean containsKey(String label) {
        return map.containsKey(label);
    }

    @Override
    public TSPublicKey getPublicKey(String label) {
        return map.get(label).publicKey;
    }

    @Override
    public KeyMetaInfo getKeyMetaInfo(String label) {
        return map.get(label).keyMetaInfo;
    }

}
