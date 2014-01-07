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

import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.Session;
import cl.niclabs.cb.backend.SessionManager;
import cl.niclabs.cb.backend.Signer;
import cl.niclabs.cb.backend.methods.SignInitMethod;
import cl.niclabs.cb.jcrypto.SessionManagerImpl;

class SignInitMethodImpl implements SignInitMethod {
    private final String sessionHandler;
    private final String mechanism;
    private final String privateKeyHandler;
    private final SessionManager sessionManager;

    public SignInitMethodImpl(Args args, SessionManager sessionManager) {
        sessionHandler = args.sessionHandler;
        mechanism = args.mechanism;
        privateKeyHandler = args.keyHandler;
        this.sessionManager = sessionManager;
    }

    @Override
    public ResponseMessage execute() {
        SessionManager sm = sessionManager;
        Session session = sm.getSession(sessionHandler);

        if (session == null) {
            return ResponseMessage.ErrorMessage("Bad session handler.");
        }

        Signer signer = session.getSigner();
        try {
            signer.init(this.mechanism, this.privateKeyHandler);
            return ResponseMessage.OKMessage();
        } catch (Exception e) {
            return ResponseMessage.ErrorMessage(e.getLocalizedMessage());
        }
    }
}
