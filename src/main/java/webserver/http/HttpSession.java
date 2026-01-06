package webserver.http;

import db.SessionStore;

import java.util.HashMap;
import java.util.Map;

public class HttpSession {

    Map<String, Object> sessionData = new HashMap<String, Object>();
    String sid;

    public HttpSession(String sid){
        this.sid = sid;
    }

    public Object getAttribute(String key) {
        return sessionData.get(key);
    }

    public void setAttribute(String key, Object value) {
        sessionData.put(key, value);
    }

    public String getSid() {
        return sid;
    }

    public void invalidate() {
        SessionStore.removeSession(sid);
    }
}
