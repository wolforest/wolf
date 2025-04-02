package com.wolf.common.convention.ability;

import com.wolf.common.convention.worker.Visitor;

/**
 * com.wolf.common.lang.contract
 *
 * @author Wingle
 * @since 2020/12/2 12:01 下午
 **/
public interface Visitable {
     void accept(Visitor<? extends Visitable> visitor);
}
