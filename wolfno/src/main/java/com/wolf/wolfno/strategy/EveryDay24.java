package com.wolf.wolfno.strategy;

import com.wolf.wolfno.factory.AbstractWolfNoCreator;
import com.wolf.wolfno.factory.WolfNoCreator;
import java.time.format.DateTimeFormatter;

public class EveryDay24 extends AbstractWolfNoCreator implements WolfNoCreator {
    private static final DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern("MMdd");

    @Override
    public String create() {
        StringBuilder sb = new StringBuilder();

        int year = context.getCreateTime().getYear() % 1000;
        if (year < 10) {
            year = 50 + year;
        }

        sb.append(year);
        sb.append(DAY_FORMATTER.format(context.getCreateTime()));
        sb.append(String.format("%03d", context.getType()));
        sb.append(getWolfID());
        sb.append(formatDatacenter());
        sb.append(formatShard());

        return sb.toString();
    }
}
