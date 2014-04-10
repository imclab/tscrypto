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

import cl.niclabs.cb.common.Method;
import cl.niclabs.cb.common.ResponseMessage;
import cl.niclabs.cb.common.SessionManager;

public class CloseSessionMethod implements Method {

    public static class Args {
        public final String sessionHandler;

        public Args(String sessionHandler) {
            this.sessionHandler = sessionHandler;
        }
    }

    private final String sessionHandler;
    private final SessionManager sessionManager;

    public CloseSessionMethod(Args args, SessionManager sessionManager) {
        sessionHandler = args.sessionHandler;
        this.sessionManager = sessionManager;
    }

    @Override
    public ResponseMessage execute() {
        SessionManager sm = sessionManager;
        sm.deleteSession(sessionHandler);
        return ResponseMessage.OKMessage();
    }
}
