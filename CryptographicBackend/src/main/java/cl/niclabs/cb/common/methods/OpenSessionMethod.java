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

import cl.niclabs.cb.common.*;

public class OpenSessionMethod implements Method {
    public static class ReturnValue {
        public final String sessionHandler;

        public ReturnValue(String sessionHandler) {
            this.sessionHandler = sessionHandler;
        }
    }

    private final SessionManager sessionManager;
    private final SessionFactory sessionFactory;

    public OpenSessionMethod(SessionManager sessionManager, SessionFactory sessionFactory) {
        this.sessionManager = sessionManager;
        this.sessionFactory = sessionFactory;
    }

    @Override
    public ResponseMessage execute() {
        SessionManager sm = sessionManager;
        Session session = sessionFactory.makeSession();

        sm.addSession(session);

        return ResponseMessage.OKMessage(new ReturnValue(session.getHandler()));
    }
}
