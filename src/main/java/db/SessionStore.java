package db;

import webserver.http.HttpSession;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionStore {
    private static Map<String, HttpSession> sessions = new HashMap<>();


    public static HttpSession addSession() {
        String sid = UUID.randomUUID().toString();
        HttpSession session = new HttpSession(sid);
        sessions.put(sid, session);
        return session;
    }

    public static HttpSession getSession() {
        return addSession();
    }

    public static HttpSession getSession(String sid) {
        return sessions.get(sid);
    }

    public static void removeSession(String sid) {
        sessions.remove(sid);
    }
}
