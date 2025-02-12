package cn.coderule.common.util.lang.time;

import cn.coderule.common.lang.enums.unit.PeriodStrategyEnum;
import cn.coderule.common.lang.exception.lang.EnumNotFoundException;

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

        int plusDays = switch (strategy) {
            case OPEN_OPEN -> 1;
            case CLOSE_CLOSE -> -1;
            default -> 0;
        };

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

        int extraDays = switch (strategy) {
            case OPEN_OPEN -> -1;
            case CLOSE_CLOSE -> 1;
            default -> 0;
        };

        if (days < 0) {
            extraDays = -1 * extraDays;
        }
        days = days + extraDays;

        return (int) days;
    }

    private static int sameDayCount(PeriodStrategyEnum strategy) {
        return switch (strategy) {
            case OPEN_OPEN -> 0;
            case OPEN_CLOSE, CLOSE_OPEN, CLOSE_CLOSE -> 1;
            default -> throw new EnumNotFoundException("Unsupported PeriodStrategyEnum:" + strategy.getName());
        };
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
