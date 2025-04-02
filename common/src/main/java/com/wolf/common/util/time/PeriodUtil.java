package com.wolf.common.util.time;

import com.wolf.common.lang.enums.PeriodStrategyEnum;
import com.wolf.common.lang.exception.enums.EnumNotFoundException;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * com.wolf.common.util
 *
 * @author Wingle
 * @since 2019/12/20 9:29 上午
 **/
public class PeriodUtil {
    public static LocalDate daysAfter(int days, PeriodStrategyEnum strategy) {
        return daysAfter(days, strategy, null);
    }

    public static LocalDate daysAfter(int days, PeriodStrategyEnum strategy, LocalDate start) {
        if (start == null) {
            start = LocalDate.now();
        }

        if (0 == days) {
            return start;
        }

        int plusDays;
        switch (strategy) {
            case OPEN_OPEN:
                plusDays = 1;
                break;
            case CLOSE_CLOSE:
                plusDays = -1;
                break;
            default:
                plusDays = 0;
        }

        if (days > 0) {
            days = days + plusDays;
        } else {
            days = days + plusDays * -1;
        }

        return start.plusDays(days);
    }

    public static int daysBetween(LocalDate start, LocalDate end, PeriodStrategyEnum strategy) {
        long days = ChronoUnit.DAYS.between(start, end);
        if (0 == days) {
            return sameDayCount(strategy);
        }

        int extraDays;
        switch (strategy) {
            case OPEN_OPEN:
                extraDays = -1;
                break;
            case CLOSE_CLOSE:
                extraDays = 1;
                break;
            default:
                extraDays = 0;
        }

        if (days < 0) {
            extraDays = -1 * extraDays;
        }
        days = days + extraDays;

        return (int) days;
    }

    private static int sameDayCount(PeriodStrategyEnum strategy) {
        int days;
        switch (strategy) {
            case OPEN_OPEN:
                days = 0;
                break;
            case OPEN_CLOSE:
            case CLOSE_OPEN:
            case CLOSE_CLOSE:
                days = 1;
                break;
            default:
                throw new EnumNotFoundException("Unsupported PeriodStrategyEnum:" + strategy.getName());
        }

        return days;
    }

    public static List<LocalDateTime> buildDateList(LocalDateTime start, LocalDateTime end, boolean boundary) {
        List<LocalDateTime> list = new ArrayList<>();

        LocalDate startDate = start.toLocalDate();
        LocalDate endDate = end.toLocalDate();
        long days = ChronoUnit.DAYS.between(startDate, endDate);

        long i = boundary ? 0 : 1;
        long len = boundary ? days : days - 1;
        for (; i <= len; i++) {
            list.add(startDate.plusDays(i).atStartOfDay());
        }
        return list;
    }
}
