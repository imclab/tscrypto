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
import cl.niclabs.tscrypto.sigDealer.RequestManager;
import cl.niclabs.cb.backend.ResponseMessage;
import cl.niclabs.cb.backend.Session;
import cl.niclabs.cb.backend.SessionManager;
import cl.niclabs.cb.backend.methods.OpenSessionMethod;
import cl.niclabs.cb.tscrypto.SessionImpl;

public class OpenSessionMethodImpl implements OpenSessionMethod {
    private KeyManager keyManager;
    private RequestManager requestManager;
    private SessionManager sessionManager;

    public OpenSessionMethodImpl(KeyManager keyManager, RequestManager requestManager, SessionManager sessionManager) {
        this.keyManager = keyManager;
        this.requestManager = requestManager;
        this.sessionManager = sessionManager;
    }

    @Override
    public ResponseMessage execute() {
        SessionManager sm = sessionManager;
        Session session = new SessionImpl(keyManager, requestManager);
        sm.addSession(session);

        return ResponseMessage.OKMessage(new ReturnValue(session.getHandler()));
    }
}
