package webserver.http;


public class Cookie {
    private final String name;
    private final String value;
    private String path = "/";
    private boolean secure = false;
    private boolean httpOnly = false;
    private String sameSite;
    private String domain;
    private int maxAge = 3600;


    public Cookie(String value) {
        this.name = "sid";
        this.value = value;
    }

    public Cookie(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public void setHttpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(name);
        sb.append("=");
        sb.append(value);

        if (path != null) {
            sb.append("; Path=").append(path);
        }

        if (domain != null && !domain.isBlank()) {
            sb.append("; Domain=").append(domain);
        }
        sb.append("; Max-Age=").append(maxAge);

        if (secure) {
            sb.append("; Secure");
        }

        if (httpOnly) {
            sb.append("; HttpOnly");
        }

        if (sameSite != null && !sameSite.isBlank()) {
            sb.append("; SameSite=").append(sameSite);
        }

        return sb.toString();
    }


}
