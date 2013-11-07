package cl.niclabs.cb.backend;

public interface SessionManager {
    Session getSession(String handler);

    void addSession(Session session);

    void deleteSession(String handler);
}
