package db;

import webserver.http.HttpSession;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionStore {
    private static Map<String, HttpSession> sessions = new HashMap<>();

    private static HttpSession addSession() {
        String key = UUID.randomUUID().toString();
        HttpSession session = new HttpSession(key);
        sessions.put(key, session);
        return session;
    }

    public static HttpSession getSession() {
        return addSession();
    }

    public static HttpSession getSession(String key) {
        return sessions.get(key);
    }

    public static void removeSession(String key) {
        sessions.remove(key);
    }
}
