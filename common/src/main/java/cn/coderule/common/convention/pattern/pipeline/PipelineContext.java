package cn.coderule.common.convention.pattern.pipeline;

import cn.coderule.common.convention.container.Context;

public interface PipelineContext extends Context {

    boolean isTerminate();

    void terminate();
}
