package com.wolf.common.lang.contract.worker;

import com.wolf.common.lang.contract.ability.Executable;
import com.wolf.common.lang.contract.ability.Retryable;

/**
 * com.wolf.common.lang.contract.worker
 *
 * @author Wingle
 * @since 2021/11/19 上午12:48
 **/
public interface Task extends Executable, Retryable {
}