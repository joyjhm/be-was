package model;

import java.util.List;

public record Page<T>(
        int page,
        int pageSize,
        boolean hasNext,
        List<T> data
)
{ }
