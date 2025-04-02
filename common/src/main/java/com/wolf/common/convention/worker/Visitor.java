package com.wolf.common.convention.worker;

/**
 * com.wolf.common.lang.contract
 *
 * @author Wingle
 * @since 2020/12/2 12:02 下午
 **/
public interface Visitor<T> {
    void visit(T visitable);
}
