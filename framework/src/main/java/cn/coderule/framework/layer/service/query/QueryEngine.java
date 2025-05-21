package cn.coderule.framework.layer.service.query;

import cn.coderule.common.convention.container.Context;
import cn.coderule.common.lang.exception.lang.IllegalArgumentException;
import cn.coderule.common.util.lang.collection.CollectionUtil;
import cn.coderule.common.util.lang.collection.MapUtil;
import cn.coderule.common.util.lang.bean.BeanUtil;
import cn.coderule.common.util.lang.string.StringUtil;
import cn.coderule.framework.layer.api.dto.PageRequest;
import cn.coderule.framework.layer.api.dto.Request;
import cn.coderule.framework.layer.api.dto.Response;
import cn.coderule.framework.context.WolfContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class QueryEngine<T extends Response, R extends Request, C extends Context> implements QueryChain<T, R, C> {
    /**
     * empty handler
     * <p>
     * if it is null and query isEmpty
     * execute defaultHandler
     */
    private QueryHandler<T, R, C> emptyHandler;

    /**
     * default handler
     * <p>
     * if not set return null
     */
    private QueryHandler<T, R, C> defaultHandler;

    /**
     * handler chain
     */
    private final List<QueryHandler<T, R, C>> chain;

    /**
     * list of key set of chain
     * <p>
     * share same index with handler chain
     */
    private final List<HashSet<String>> keyChain;

    /**
     * exactHandlerMap
     * <p>
     * existing exactHandler will have the value true
     */
    private final Map<QueryHandler<T, R, C>, Boolean> exactHandlerMap;

    public QueryEngine() {
        emptyHandler = null;
        chain = new ArrayList<>();
        keyChain = new ArrayList<>();
        exactHandlerMap = new HashMap<>();
    }

    @Override
    public <Y extends QueryHandler<T, R, C>> QueryChain<T, R, C> addEmptyHandler(@NonNull Class<Y> cls) {
        QueryHandler<T, R, C> filter = WolfContext.getBean(cls);
        if (this.emptyHandler != null) {
            throw new IllegalArgumentException("already have empty filter");
        }
        this.emptyHandler = filter;
        return this;
    }

    @Override
    public <Y extends QueryHandler<T, R, C>> QueryChain<T, R, C> addDefaultHandler(@NonNull Class<Y> cls) {
        QueryHandler<T, R, C> filter = WolfContext.getBean(cls);

        if (this.defaultHandler != null) {
            throw new IllegalArgumentException("already have default filter");
        }
        this.defaultHandler = filter;
        return this;
    }

    @Override
    public <Y extends QueryHandler<T, R, C>> QueryChain<T, R, C> addHandler(@NonNull Class<Y> cls, String... keys) {
        QueryHandler<T, R, C> filter = WolfContext.getBean(cls);

        List<String> keysList = Arrays.asList(keys);
        if (keysList.size() == 0) {
            throw new IllegalArgumentException("QueryHandler must have one match key");
        }

        chain.add(filter);
        HashSet<String> hashSet = new HashSet<>(keysList);
        keyChain.add(hashSet);
        exactHandlerMap.put(filter, false);

        return this;
    }

    @Override
    public <Y extends CustomQueryHandler<T, R, C>> QueryChain<T, R, C> addCustomHandler(@NonNull Class<Y> cls) {
        QueryHandler<T, R, C> filter = WolfContext.getBean(cls);

        chain.add(filter);
        keyChain.add(new HashSet<>());
        exactHandlerMap.put(filter, false);

        return this;
    }

    @Override
    public <Y extends QueryHandler<T, R, C>> QueryChain<T, R, C> addExactHandler(@NonNull Class<Y> cls, String... keys) {
        QueryHandler<T, R, C> filter = WolfContext.getBean(cls);

        chain.add(filter);
        HashSet<String> hashSet = new HashSet<>(Arrays.asList(keys));
        keyChain.add(hashSet);
        exactHandlerMap.put(filter, true);

        return this;
    }

    @Override
    public T execute(R request, C context) {
        Map<String, Object> requestMap = prepareRequest(request);

        if (null != emptyHandler && MapUtil.isEmpty(requestMap)) {
            log.info("Query Engine: Empty Handler: {} Request: {}", emptyHandler, request);
            return emptyHandler.handle(request, context);
        }

        for (int i = 0; i < chain.size(); i++) {
            QueryHandler<T, R, C> filter = chain.get(i);
            HashSet<String> keySet = keyChain.get(i);

            if (filter instanceof CustomQueryHandler) {
                T response = filter.handle(request, context);
                // if custom query handler's response is null, continue to run next
                if (response != null) {
                    log.info("Query Engine: Custom Handler: {} Request: {}, Reponse: {}", filter, request, response);
                    return response;
                }
                continue;
            }

            if (isMatched(keySet, requestMap, filter)) {
                T response = filter.handle(request, context);
                log.info("Query Engine: {} Request: {} Response: {}", filter, request, response);
                return response;
            }
        }

        if (null != defaultHandler) {
            log.info("Query Engine: Default Handler: {} Request: {}", defaultHandler, request);
            return defaultHandler.handle(request, context);
        }

        throw new QueryHandlerNotFoundException();
    }

    private Map<String, Object> prepareRequest(R request) {
        Map<String, Object> requestMap = MapUtil.filterBlank(BeanUtil.toMap(request));
        // remove pageRequest property name
        ArrayList<String> ignoreList = BeanUtil.getPropertyNames(PageRequest.class);
        ignoreList.forEach(requestMap::remove);
        log.info("Query Engine: prepareRequest: Request: {} RequestMap: {}, IgnoreList: {}", request, requestMap, ignoreList);

        return requestMap;
    }

    private boolean isMatched(@NonNull HashSet<String> set, @NonNull Map<String, Object> requestMap, QueryHandler<T, R, C> filter) {
        // exact match
        if (exactHandlerMap.get(filter)) {
            return set.equals(requestMap.keySet());
        }

        for (String requestKey : set) {
            if (!requestMap.containsKey(requestKey)) {
                return false;
            }

            Object val = requestMap.get(requestKey);
            if (!isValidValue(val)) {
                return false;
            }
        }

        return true;
    }

    private boolean isValidValue(Object v) {
        if (v instanceof String) {
            return StringUtil.notBlank(v);
        }

        if (v instanceof Collection) {
            return !CollectionUtil.isEmpty((Collection<?>) v);
        }

        if (v instanceof Map) {
            return !MapUtil.isEmpty((Map<?, ?>) v);
        }

        return null != v;
    }
}
