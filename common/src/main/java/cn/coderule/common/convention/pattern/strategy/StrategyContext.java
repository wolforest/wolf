package cn.coderule.common.convention.pattern.strategy;

import cn.coderule.common.convention.container.Context;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.ClassUtils;

import java.util.List;

@Getter
@Setter
public class StrategyContext<T extends Strategy> implements Context {

    protected List<T> iStrategies;

    protected T currentStrategy;

    protected boolean success = false;

    public StrategyContext(List<T> iStrategies) {
        this.iStrategies = iStrategies;
    }

    public void success() {
        this.success = true;
    }

    public void failure() {
        this.success = false;
    }

    public <S extends Strategy> S getStrategy(Class<S> clazz) {
        Strategy strategy = iStrategies.stream().filter(item -> ClassUtils.isAssignable(item.getClass(), clazz)).findFirst().orElse(null);
        return strategy == null ? null : (S) strategy;
    }
}
