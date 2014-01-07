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

import cl.niclabs.tscrypto.common.datatypes.Ticket;
import cl.niclabs.tscrypto.common.utils.TSLogger;
import cl.niclabs.tscrypto.sigDealer.DeleteKeyRequest;
import cl.niclabs.tscrypto.sigDealer.KeyManager;
import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.methods.DeleteKeyPairMethod;
import cl.niclabs.tscrypto.sigDealer.RequestManager;

import java.io.IOException;

class DeleteKeyPairMethodImpl implements DeleteKeyPairMethod {
    private final String keyHandler;
    private final KeyManager keyManager;
    private final RequestManager requestManager;

    public DeleteKeyPairMethodImpl(Args args, KeyManager keyManager, RequestManager requestManager) {
        this.keyHandler = args.keyHandler;
        this.keyManager = keyManager;
        this.requestManager = requestManager;
    }

    @Override
    public ResponseMessage execute() {

        try {
            Ticket ticket = requestManager.deleteKey(keyHandler);
            DeleteKeyRequest request = requestManager.getDeleteKeyRequest(ticket);
            request.waitUntilReady();
            requestManager.removeRequest(ticket);

            keyManager.removeKey(this.keyHandler);
            return ResponseMessage.OKMessage();
        } catch (IOException | InterruptedException e) {
            return  ResponseMessage.ErrorMessage(e.getMessage());
        }
    }
}
