package com.wolf.common.convention.worker;

/**
 * com.wolf.common.lang.contract.worker
 *
 * @author Wingle
 * @since 2020/11/6 11:02 上午
 **/
public interface Locator<POSITION, TARGET> {
    TARGET locate(POSITION position);
}
