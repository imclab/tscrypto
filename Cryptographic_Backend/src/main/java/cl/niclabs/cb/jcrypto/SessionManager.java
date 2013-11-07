package cl.niclabs.cb.jcrypto;

import java.util.Hashtable;
import java.util.Map;

// Singleton
public class SessionManager {
    private static SessionManager instance = null;
    public static SessionManager getInstance() {
        if (instance == null) {
            synchronized (SessionManager.class) {
                if (instance == null) {
                    instance = new SessionManager();
                }
            }
        }

        return instance;
    }

    private Map<String, Session> sessions;

    public SessionManager() {
        this.sessions = new Hashtable<>();
    }

    public Session getSession(String handler) {
        return sessions.get(handler); // Check for nullity
    }

    public void addSession(Session session) {
        sessions.put(session.getHandler(), session);
    }

    public void deleteSession(String handler) {
        sessions.remove(handler);
    }

}
