package cn.coderule.framework.layer.service.task;

import cn.coderule.common.util.lang.StringUtil;
import cn.coderule.common.util.lang.time.DateUtil;
import cn.coderule.framework.layer.api.dto.Request;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Setter;

@Setter
public class ScanTaskRequest implements Request {
    private static final int DEFAULT_BATCH_SIZE = 100;
    private static final int DEFAULT_THREAD_NUM = 1;
    private String dryRun;

    private String start;
    private String end;

    private int threadNum;
    private int batchSize;

    public LocalDate getStartDate() {
        if (StringUtil.isBlank(start)) {
            return null;
        }

        return DateUtil.asLocalDate(start);
    }

    public LocalDate getEndDate() {
        if (StringUtil.isBlank(end)) {
            return null;
        }

        return DateUtil.asLocalDate(end);
    }

    public LocalDateTime getStartDateTime() {
        if (StringUtil.isBlank(start)) {
            return null;
        }

        return DateUtil.asLocalDateTime(start);
    }

    public LocalDateTime getEndDateTime() {
        if (StringUtil.isBlank(end)) {
            return null;
        }

        return DateUtil.asLocalDateTime(end);
    }

    public boolean isDryRun() {
        return !StringUtil.isFalse(dryRun);
    }

    public int getThreadNum() {
        if (threadNum <= 0) {
            return DEFAULT_THREAD_NUM;
        }

        return threadNum;
    }

    public int getBatchSize() {
        if (batchSize <= 0) {
            return DEFAULT_BATCH_SIZE;
        }

        return batchSize;
    }

}
