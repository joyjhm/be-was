package webserver.http;

import db.SessionStore;

import java.util.HashMap;
import java.util.Map;

public class HttpSession {

    Map<String, Object> sessionData = new HashMap<String, Object>();
    String key;

    public HttpSession(String key){
        this.key = key;
    }

    public Object getAttribute(String key) {
        return sessionData.get(key);
    }

    public void setAttribute(String key, Object value) {
        sessionData.put(key, value);
    }

    public String getKey() {
        return key;
    }

    public void invalidate() {
        SessionStore.removeSession(key);
    }
}
