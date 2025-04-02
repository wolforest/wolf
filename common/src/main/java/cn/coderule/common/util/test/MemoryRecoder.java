package cn.coderule.common.util.test;

import cn.coderule.common.lang.enums.unit.DigitalUnitEnum;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Getter;

public class MemoryRecoder {
    private final DigitalUnitEnum unit;
    @Getter
    private final Map<String, Double> recordMap;
    private double start;

    public MemoryRecoder() {
        this(DigitalUnitEnum.KB);
    }

    public MemoryRecoder(DigitalUnitEnum unit) {
        this.unit = unit;
        recordMap = new LinkedHashMap<>();
    }

    public void start() {
        System.gc();
        start = getMemory();
    }

    public void record(String name) {
        double end = getMemory();
        recordMap.put(name, (end - start));
    }

    public Double getUsage() {
        return getMemory() - start;
    }

    public Double getRecord(String name) {
        return recordMap.get(name);
    }

    public void clear() {
        recordMap.clear();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n\n/*")
            .append("*".repeat(20))
            .append(" Memory Usage Recorder Start ")
            .append("*".repeat(20))
            .append("*/\n");

        for (Map.Entry<String, Double> entry : recordMap.entrySet()) {
            sb.append(entry.getKey())
                .append(" memory usage: ")
                .append(String.format("%.2f", entry.getValue()))
                .append(unit.getName())
                .append("; \n");
        }

        sb.append("/*")
            .append("*".repeat(20))
            .append(" Memory Usage Recorder End   ")
            .append("*".repeat(20))
            .append("*/\n\n");

        return sb.toString();
    }

    private double getMemory() {
        double usage = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory() - start) / unit.getCode();

        return switch (unit) {
            case B -> usage;
            case KB -> usage / 1024;
            case MB -> usage / 1024 / 1024;
            case GB -> usage / 1024 / 1024 / 1024;
            case TB -> usage / 1024 / 1024 / 1024 / 1024;
            case PB -> usage / 1024 / 1024 / 1024 / 1024 / 1024;
        };
    }
}
