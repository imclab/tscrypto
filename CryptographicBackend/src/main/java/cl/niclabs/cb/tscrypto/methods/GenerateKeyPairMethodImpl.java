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

import cl.niclabs.tscrypto.common.datatypes.KeyInfo;
import cl.niclabs.tscrypto.common.datatypes.Ticket;
import cl.niclabs.tscrypto.common.utils.TSLogger;
import cl.niclabs.tscrypto.sigDealer.KeyDispatchRequest;
import cl.niclabs.tscrypto.keyFactory.algorithm.KeyFactory;
import cl.niclabs.tscrypto.sigDealer.KeyManager;
import cl.niclabs.tscrypto.sigDealer.RequestManager;
import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.methods.GenerateKeyPairMethod;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;

class GenerateKeyPairMethodImpl implements GenerateKeyPairMethod {
    private final int keySize;
    private final KeyManager keyManager;
    private final RequestManager requestManager;
    private final int k;
    private final int l;

    public GenerateKeyPairMethodImpl(Args args, KeyManager keyManager, RequestManager requestManager, int k, int l) {
        keySize = args.keySize;
        this.keyManager = keyManager;
        this.requestManager = requestManager;
        this.k = k;
        this.l = l;
    }

    @Override
    public ResponseMessage execute() {
        try {
            TSLogger.keyFactory.debug("KeyPair Generation with k="+k+" and l="+l);
            KeyInfo keyInfo = KeyFactory.generateKeys(this.keySize, k, l);
            TSLogger.keyFactory.debug("KeyPair Generation successful");
            String handler = keyInfo.getKeyMetaInfo().getAlias();
            Ticket ticket = requestManager.dispatchKey(keyInfo);
            KeyDispatchRequest request = requestManager.getKeyDispatchRequest(ticket);

            request.waitUntilReady();

            requestManager.removeRequest(ticket);
            keyManager.addKey(keyInfo);
            String modulus = DatatypeConverter.printBase64Binary(keyInfo.getPublicKey().getModulus().toByteArray());
            String publicExponent = DatatypeConverter.printBase64Binary(keyInfo.getPublicKey().getExponent().toByteArray());
            return ResponseMessage.OKMessage(new ReturnValue(handler, modulus, publicExponent));
        } catch (IOException | InterruptedException e) {
            return  ResponseMessage.ErrorMessage(e.getMessage());
        }
    }
}
