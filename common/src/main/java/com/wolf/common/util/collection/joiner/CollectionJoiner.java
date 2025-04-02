package com.wolf.common.util.collection.joiner;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * com.wolf.common.util.collection.joiner
 *
 * @author Wingle
 * @since 2020/5/19 5:05 下午
 **/
public class CollectionJoiner<BASE> {
    Collection<BASE> baseList;

    public static <BASE> CollectionJoiner<BASE>  base(Collection<BASE> base) {
        return new CollectionJoiner<>(base);
    }

    public CollectionJoiner(Collection<BASE> base) {
        baseList = base;
    }

    @SafeVarargs
    public final <EXT> Joiner<BASE, EXT> on(BiConsumer<BASE, EXT> setter, Function<BASE, Object>... getters) {
        return new DefaultJoiner<BASE, EXT>(baseList).on(setter, getters);
    }
}
