package com.wolf.common.convention.pattern.pipeline;

import com.wolf.common.convention.container.Context;

public interface PipelineContext extends Context {

    boolean isTerminate();

    void terminate();
}
