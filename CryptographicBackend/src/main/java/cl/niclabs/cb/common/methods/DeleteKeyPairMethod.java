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

package cl.niclabs.cb.common.methods;

import cl.niclabs.cb.common.KeyOperations;
import cl.niclabs.cb.common.Method;
import cl.niclabs.cb.common.ResponseMessage;

public class DeleteKeyPairMethod implements Method {
    public static class Args {
        public final String keyHandler;

        public Args(String keyHandler) {
            this.keyHandler = keyHandler;
        }
    }

    private final String handler;
    private final KeyOperations keyOperations;

    public DeleteKeyPairMethod(Args args, KeyOperations keyOperations) {
        handler = args.keyHandler;
        this.keyOperations = keyOperations;
    }

    @Override
    public ResponseMessage execute() {
        try {
            keyOperations.deleteKeys(handler);
            return ResponseMessage.OKMessage();
        } catch (Exception e) {
            return ResponseMessage.ErrorMessage(e.getMessage());
        }
    }
}
