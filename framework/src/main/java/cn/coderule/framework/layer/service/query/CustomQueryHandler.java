package cn.coderule.framework.layer.service.query;

import cn.coderule.common.convention.container.Context;
import cn.coderule.framework.layer.api.dto.Request;
import cn.coderule.framework.layer.api.dto.Response;

/**
 * QueryHandler
 */
public interface CustomQueryHandler<T extends Response, R extends Request, C extends Context> extends QueryHandler<T, R, C> {
}
