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

package cl.niclabs.cb.jcrypto;

import cl.niclabs.cb.backend.Session;
import cl.niclabs.cb.backend.SessionManager;

import java.util.Hashtable;
import java.util.Map;

// Singleton
public class SessionManagerImpl implements SessionManager {
    private static SessionManager instance = null;
    public static SessionManager getInstance() { // For use on jcrypto
        if (instance == null) {
            synchronized (SessionManagerImpl.class) {
                if (instance == null) {
                    instance = new SessionManagerImpl();
                }
            }
        }

        return instance;
    }

    private Map<String, Session> sessions;

    public SessionManagerImpl() {
        this.sessions = new Hashtable<>();
    }

    @Override
    public Session getSession(String handler) {
        return sessions.get(handler); // Check for nullity
    }

    @Override
    public void addSession(Session session) {
        sessions.put(session.getHandler(), session);
    }

    @Override
    public void deleteSession(String handler) {
        sessions.remove(handler);
    }

}
