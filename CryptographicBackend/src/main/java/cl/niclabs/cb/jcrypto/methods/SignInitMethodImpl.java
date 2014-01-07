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

package cl.niclabs.cb.jcrypto.methods;

import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.Session;
import cl.niclabs.cb.backend.SessionManager;
import cl.niclabs.cb.backend.Signer;
import cl.niclabs.cb.backend.methods.SignInitMethod;
import cl.niclabs.cb.jcrypto.SessionManagerImpl;

import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

class SignInitMethodImpl implements SignInitMethod {
    private String sessionHandler;
    private String mechanism;
    private String handler;


    public SignInitMethodImpl(Args args) {
        sessionHandler = args.sessionHandler;
        mechanism = args.mechanism;
        handler = args.keyHandler;
    }

    @Override
    public ResponseMessage execute() {
        SessionManager sm = SessionManagerImpl.getInstance();
        Session session = sm.getSession(sessionHandler);

        if (session == null) {
            return ResponseMessage.ErrorMessage("Bad session handler.");
        }

        Signer signer = session.getSigner();
        try {
            signer.init(mechanism, handler);
            return ResponseMessage.OKMessage();
        }
        catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e) {
            return ResponseMessage.ErrorMessage(e.getLocalizedMessage());
        }
    }
}
