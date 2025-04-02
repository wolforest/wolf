package cn.coderule.common.sm.impl;

import cn.coderule.common.sm.State;

import java.util.HashMap;

/**
 * com.wolf.common.sm.impl
 *
 * @author Wingle
 * @since 2020/12/2 1:26 上午
 **/
public  class StateMap<S extends State> extends HashMap<Integer, S> {

    public S put(Integer key, S value) {
        S v = get(key);
        if (null != v) {
            return v;
        }

        return super.put(key, value);
    }
}
