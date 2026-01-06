package webserver;

import java.util.HashMap;
import java.util.Map;

public class Model {

    Map<String, String> attributes = new HashMap<>();

    public void setAttribute(String key, String value) {
        this.attributes.put(key, value);
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

}
