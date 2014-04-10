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
import cl.niclabs.cb.common.Session;
import cl.niclabs.cb.common.SessionManager;

import javax.xml.bind.DatatypeConverter;

public class GenerateRandomMethod implements Method {
    public static class Args {
        public final String sessionHandler;
        public final int length;

        public Args(String sessionHandler, int length) {
            this.sessionHandler = sessionHandler;
            this.length = length;
        }
    }

    public static class ReturnValue {
        public final String data;

        public ReturnValue(String data) {
            this.data = data;
        }
    }

    private final String sessionHandler;
    private final int length;
    private final SessionManager sessionManager;

    public GenerateRandomMethod(Args args, SessionManager sessionManager) {
        sessionHandler = args.sessionHandler;
        length = args.length;
        this.sessionManager = sessionManager;
    }

    @Override
    public ResponseMessage execute() {
        Session session = sessionManager.getSession(sessionHandler);
        if (session == null) {
            return ResponseMessage.ErrorMessage("Bad session handler");
        }

        byte[] randomBytes = session.getRandomGenerator().generateRandom(this.length);
        String rv = DatatypeConverter.printBase64Binary(randomBytes);
        return ResponseMessage.OKMessage(new ReturnValue(rv));
    }
}
