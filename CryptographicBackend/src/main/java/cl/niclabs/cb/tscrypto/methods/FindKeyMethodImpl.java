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

package cl.niclabs.cb.tscrypto.methods;

import cl.niclabs.tscrypto.sigDealer.KeyManager;
import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.methods.FindKeyMethod;

import javax.xml.bind.DatatypeConverter;
import java.security.PublicKey;

class FindKeyMethodImpl implements FindKeyMethod {
    protected final String handler;
    private final KeyManager keyManager;

    public FindKeyMethodImpl(Args args, KeyManager keyManager) {
        handler = args.keyHandler;
        this.keyManager = keyManager;
    }

    @Override
    public ResponseMessage execute() {
        PublicKey pk;
        try {
            pk = keyManager.getPublicKey(handler).convertoToPublicKey();
        } catch (Exception e) {
            return ResponseMessage.ErrorMessage(e.getLocalizedMessage());
        }

        String key = "-----BEGIN PUBLIC KEY-----\n";
        String keyBase64 = DatatypeConverter.printBase64Binary(pk.getEncoded());
        while(keyBase64.length() > 0) {
            try {
                key += keyBase64.substring(0, 64) + "\n";
                keyBase64 = keyBase64.substring(64);
            } catch (IndexOutOfBoundsException e) {
                key += keyBase64 + "\n";
                break;
            }
        }
        key += "-----END PUBLIC KEY-----\n";

        return ResponseMessage.OKMessage(new ReturnValue(key));
    }
}
