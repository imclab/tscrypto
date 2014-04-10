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

public class SignInitMethod implements Method {
	public static class Args {
        public final String sessionHandler;
		public final String mechanism;
		public final String keyHandler;

        public Args(String sessionHandler, String mechanism, String keyHandler) {
            this.sessionHandler = sessionHandler;
            this.mechanism = mechanism;
            this.keyHandler = keyHandler;
        }
    }

    private final String sessionHandler;
    private final String mechanism;
    private final String privateKeyHandler;
    private final SessionManager sessionManager;

    public SignInitMethod(Args args, SessionManager sessionManager) {
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
