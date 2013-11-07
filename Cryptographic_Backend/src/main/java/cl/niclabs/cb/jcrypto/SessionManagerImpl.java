package cl.niclabs.cb.jcrypto;

import cl.niclabs.cb.backend.Session;
import cl.niclabs.cb.backend.SessionManager;

import java.util.Hashtable;
import java.util.Map;

// Singleton
public class SessionManagerImpl implements SessionManager {
    private static SessionManager instance = null;
    public static SessionManager getInstance() {
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
