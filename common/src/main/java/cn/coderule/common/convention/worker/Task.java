package cn.coderule.common.convention.worker;

import cn.coderule.common.convention.ability.Executable;
import cn.coderule.common.convention.ability.Retryable;

/**
 * com.wolf.common.lang.contract.worker
 *
 * @author Wingle
 * @since 2021/11/19 上午12:48
 **/
public interface Task extends Executable, Retryable {
}
