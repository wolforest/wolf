package cn.coderule.common.util.test;

import java.util.LinkedHashMap;
import lombok.Getter;
import lombok.NonNull;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * com.wolf.common.util.time
 * not threadSafe current
 *
 * @author Wingle
 * @since 2022/1/17 下午1:03
 **/
public class Timer {
    private final TimeUnit unit;
    @Getter
    private final Map<String, Long> recordMap;
    private Long startAt;

    public Timer() {
        this(TimeUnit.MILLISECONDS);
    }

    public Timer(TimeUnit unit) {
        this.unit = unit;
        recordMap = new LinkedHashMap<>();
    }

    public void start() {
        startAt = getTime();
    }

    public void record(@NonNull String name) {
        Long end = getTime();
        recordMap.put(name, (end- startAt));

        startAt = end;
    }

    public Long getRecord(@NonNull String name) {
        return recordMap.get(name);
    }

    public Long elapse() {
        Long end = getTime();
        return (end - startAt);
    }

    public void clear() {
        recordMap.clear();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n\n/*")
            .append("*".repeat(20))
            .append(" Timer Start ")
            .append("*".repeat(20))
            .append("*/\n");

        for (Map.Entry<String, Long> entry : recordMap.entrySet()) {
            sb.append(entry.getKey())
                .append(" elapse: ")
                .append(entry.getValue())
                .append(getUnitName())
                .append("; \n");
        }

        sb.append("/*")
            .append("*".repeat(20))
            .append(" Timer End   ")
            .append("*".repeat(20))
            .append("*/\n\n");

        return sb.toString();
    }

    private long getTime() {
        switch (unit) {
            case SECONDS -> {
                return System.currentTimeMillis() / 1000;
            }
            case MILLISECONDS -> {
                return System.currentTimeMillis();
            }
            case NANOSECONDS -> {
                return System.nanoTime();
            }
        }

        throw new IllegalArgumentException("not support unit: " + unit);
    }

    private String getUnitName() {
        switch (unit) {
            case SECONDS -> {
                return "s";
            }
            case MILLISECONDS -> {
                return "ms";
            }
            case NANOSECONDS -> {
                return "ns";
            }
        }

        throw new IllegalArgumentException("not support unit: " + unit);
    }

}
