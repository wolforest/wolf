package cn.coderule.common.ds.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class ListMap<K, V> extends HashMap<K, List<V>> {

    public List<V> getList(K key) {
        List<V> list = get(key);
        if (list == null) {
            list = new ArrayList<>();
            put(key, list);
        }

        return list;
    }

    public List<V> add(K key, V value) {
        List<V> list = getList(key);

        list.add(value);
        return list;
    }

    public List<V> addAll(K key, Collection<V> values) {
        List<V> list = getList(key);

        list.addAll(values);
        return list;
    }
}
