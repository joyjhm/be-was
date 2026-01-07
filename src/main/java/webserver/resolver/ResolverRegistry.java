package webserver.resolver;

import java.util.LinkedList;
import java.util.List;

public class ResolverRegistry {

    List<HttpResolver> httpResolvers = new LinkedList<>();

    public ResolverRegistry() {
        httpResolvers.add(new SessionResolver());
    }

    public List<HttpResolver> getHttpResolvers() {
        return httpResolvers;
    }

}
