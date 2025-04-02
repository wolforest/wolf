package com.wolf.framework.layer.service.task;

import com.wolf.common.convention.container.Context;
import lombok.Data;

/**
 * @author weixing
 * @since 2022/8/17 16:10
 */
@Data
public class TaskRunningContext implements Context {
    protected int threadNum;
    protected int batchSize;
    protected boolean dryRun;
    protected String logTag;
}
