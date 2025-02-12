package cn.coderule.common.util.lang.collection.joiner;

import cn.coderule.common.util.lang.collection.joiner.exception.InvalidGetterException;
import cn.coderule.common.util.lang.collection.CollectionUtil;
import lombok.Getter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * com.wolf.common.util.collection.joiner
 *
 * @author Wingle
 * @since 2020/5/18 3:13 下午
 **/
public class DefaultJoiner<BASE, EXT> implements Joiner<BASE, EXT> {
    private static final int MIN_GETTER_LENGTH = 1;
    private static final int MAX_GETTER_LENGTH = 10;
    private static final String NULL_VALUE = "NULL_VALUE";
    private static final String KEY_DELIMITER = ":";

    @Getter
    private Collection<BASE> baseList;
    private Function<BASE, Object>[] baseGetters;
    private BiConsumer<BASE, EXT> baseSetter;

    private Map<Object, EXT> extMap;

    @SafeVarargs
    public static <BASE, EXT> Joiner<BASE, EXT> on(Collection<BASE> base, BiConsumer<BASE, EXT> setter, Function<BASE, Object>... getters) {
        Joiner <BASE, EXT> joiner = new DefaultJoiner<>(base);
        return joiner.on(setter, getters);
    }

    public DefaultJoiner(Collection<BASE> baseList) {
        this.baseList = baseList;
    }

    @SafeVarargs
    @Override
    public final Joiner<BASE, EXT> on(BiConsumer<BASE, EXT> setter, Function<BASE, Object>... getters) {
        validBaseGetter(getters);

        baseSetter = setter;
        baseGetters = getters;

        return this;
    }

    @SafeVarargs
    @Override
    public final CollectionJoiner<BASE> join(Collection<EXT> extList, Function<EXT, Object>... getters) {
        if (CollectionUtil.isEmpty(extList)) {
            return new CollectionJoiner<>(baseList);
        }

        validExtGetter(getters);
        formatExtMap(extList, getters);
        joinExtMap();

        return new CollectionJoiner<>(baseList);
    }

    private void joinExtMap() {
        if (CollectionUtil.isEmpty(baseList)) {
            return;
        }

        Object key;
        EXT ext;
        for (BASE base : baseList) {
            key = getBaseKey(base);
            ext = extMap.get(key);
            if (ext == null) {
                continue;
            }
            baseSetter.accept(base, ext);
        }
    }

    @SafeVarargs
    private final void formatExtMap(Collection<EXT> extList, Function<EXT, Object>... getters) {
        extMap = new HashMap<>(extList.size());

        Object key;
        for (EXT ext : extList) {
            key = getExtKey(ext, getters);
            extMap.put(key, ext);
        }
    }

    @SafeVarargs
    private final void validExtGetter(Function<EXT, Object>... getters) {
        int len = getters.length;
        if (len < MIN_GETTER_LENGTH || len > MAX_GETTER_LENGTH) {
            throw new InvalidGetterException(len);
        }
    }

    @SafeVarargs
    private final void validBaseGetter(Function<BASE, Object>... getters) {
        int len = getters.length;
        if (len < MIN_GETTER_LENGTH || len > MAX_GETTER_LENGTH) {
            throw new InvalidGetterException(len);
        }
    }

    private Object getBaseKey(BASE base) {
        Object value;
        if (1 == baseGetters.length) {
            value = baseGetters[0].apply(base);
            return formatValue(value);
        }

        StringBuilder sb = new StringBuilder();
        for (Function<BASE, Object> getter : baseGetters) {
            value = getter.apply(base);
            sb.append(value).append(KEY_DELIMITER);
        }

        return sb.toString();
    }

    @SafeVarargs
    private final Object getExtKey(EXT ext, Function<EXT, Object>... getters) {
        Object value;
        if (1 == getters.length) {
            value = getters[0].apply(ext);
            return formatValue(value);
        }

        StringBuilder sb = new StringBuilder();
        for (Function<EXT, Object> getter : getters) {
            value = getter.apply(ext);
            sb.append(value).append(KEY_DELIMITER);
        }

        return sb.toString();
    }

    private Object formatValue(Object value) {
        if (value != null) {
            return value;
        }

        return NULL_VALUE;
    }
}
