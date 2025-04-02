package cn.coderule.common.convention.ability;

import cn.coderule.common.convention.worker.Visitor;

/**
 * com.wolf.common.lang.contract
 *
 * @author Wingle
 * @since 2020/12/2 12:01 下午
 **/
public interface Visitable {
     void accept(Visitor<? extends Visitable> visitor);
}
