package webserver.filter;


import java.util.ArrayList;
import java.util.List;

public class FilterRegistry {

    List<Filter> filters = new ArrayList<>();

    public FilterRegistry() {
        filters.add(new AuthFilter());
    }

    public List<Filter> getFilters() {
        return filters;
    }
}
