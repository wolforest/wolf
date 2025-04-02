package com.wolf.common.convention.pattern.pipeline;

import com.wolf.common.convention.pattern.factory.Factory;

public interface PipelineFactory<T extends PipelineContext> extends Factory {

    Pipeline<T> initPipeline();
}
