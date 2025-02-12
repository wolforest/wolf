package cn.coderule.common.convention.pattern.pipeline;

import cn.coderule.common.convention.pattern.factory.Factory;

public interface PipelineFactory<T extends PipelineContext> extends Factory {

    Pipeline<T> initPipeline();
}
