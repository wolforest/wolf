package com.wolf.framework.layer.service.query;

import com.wolf.common.lang.exception.SystemException;

public class QueryHandlerNotFoundException extends SystemException {
    private static final String DEFAULT_MESSAGE = "QueryHandlerNotFoundException";

    public QueryHandlerNotFoundException() {
        super(500, DEFAULT_MESSAGE);
    }

    public QueryHandlerNotFoundException(String message) {
        super(500, message);
    }
}
