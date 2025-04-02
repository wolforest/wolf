package com.wolf.framework.layer.service.query;

import com.wolf.common.convention.container.Context;
import com.wolf.framework.layer.api.dto.Request;
import com.wolf.framework.layer.api.dto.Response;

/**
 * QueryHandler
 */
public interface CustomQueryHandler<T extends Response, R extends Request, C extends Context> extends QueryHandler<T, R, C> {
}
