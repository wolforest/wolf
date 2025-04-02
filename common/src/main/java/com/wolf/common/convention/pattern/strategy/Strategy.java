package com.wolf.common.convention.pattern.strategy;

public interface Strategy<T extends StrategyContext> {

    void execute(T context);
}
