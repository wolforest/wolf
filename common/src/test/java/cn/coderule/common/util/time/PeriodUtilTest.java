package cn.coderule.common.util.time;

import cn.coderule.common.util.lang.time.PeriodUtil;
import org.junit.Test;
import cn.coderule.common.lang.enums.unit.PeriodStrategyEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * com.wolf.common.util
 *
 * @author Wingle
 * @since 2019/12/20 9:50 上午
 **/
public class PeriodUtilTest {

    @Test
    public void sameDaysBetween() {
        LocalDate start, end;
        int days, expected;

        //case 1
        start = LocalDate.of(2019, 1, 1);
        end = LocalDate.of(2019, 1, 1);

        expected = 1;
        days = PeriodUtil.daysBetween(start, end, PeriodStrategyEnum.CLOSE_CLOSE);
        assertEquals("same day count daysBetween: close close strategy fail", expected, days);

        expected = 1;
        days = PeriodUtil.daysBetween(start, end, PeriodStrategyEnum.CLOSE_OPEN);
        assertEquals("same day count daysBetween: close close strategy fail", expected, days);

        expected = 1;
        days = PeriodUtil.daysBetween(start, end, PeriodStrategyEnum.OPEN_CLOSE);
        assertEquals("same day count daysBetween: close close strategy fail", expected, days);

        expected = 0;
        days = PeriodUtil.daysBetween(start, end, PeriodStrategyEnum.OPEN_OPEN);
        assertEquals("same day count daysBetween: close close strategy fail", expected, days);
    }

    @Test
    public void smallDaysBetween() {
        LocalDate start, end;
        int days, expected;

        //case 1
        start = LocalDate.of(2019, 1, 1);
        end = LocalDate.of(2019, 1, 10);

        expected = 10;
        days = PeriodUtil.daysBetween(start, end, PeriodStrategyEnum.CLOSE_CLOSE);
        assertEquals("same day count daysBetween: close close strategy fail", expected, days);

        expected = 9;
        days = PeriodUtil.daysBetween(start, end, PeriodStrategyEnum.CLOSE_OPEN);
        assertEquals("same day count daysBetween: close close strategy fail", expected, days);

        expected = 9;
        days = PeriodUtil.daysBetween(start, end, PeriodStrategyEnum.OPEN_CLOSE);
        assertEquals("same day count daysBetween: close close strategy fail", expected, days);

        expected = 8;
        days = PeriodUtil.daysBetween(start, end, PeriodStrategyEnum.OPEN_OPEN);
        assertEquals("same day count daysBetween: close close strategy fail", expected, days);
    }

    @Test
    public void bigDaysBetween() {
        LocalDate start, end;
        int days, expected;

        //case 1
        start = LocalDate.of(2019, 1, 1);
        end = LocalDate.of(2019, 2, 10);

        expected = 41;
        days = PeriodUtil.daysBetween(start, end, PeriodStrategyEnum.CLOSE_CLOSE);
        assertEquals("same day count daysBetween: close close strategy fail", expected, days);

        expected = 40;
        days = PeriodUtil.daysBetween(start, end, PeriodStrategyEnum.CLOSE_OPEN);
        assertEquals("same day count daysBetween: close close strategy fail", expected, days);

        expected = 40;
        days = PeriodUtil.daysBetween(start, end, PeriodStrategyEnum.OPEN_CLOSE);
        assertEquals("same day count daysBetween: close close strategy fail", expected, days);

        expected = 39;
        days = PeriodUtil.daysBetween(start, end, PeriodStrategyEnum.OPEN_OPEN);
        assertEquals("same day count daysBetween: close close strategy fail", expected, days);
    }

    @Test
    public void largeDaysBetween() {
        LocalDate start, end;
        int days, expected;

        //case 1
        start = LocalDate.of(2018, 1, 1);
        end = LocalDate.of(2019, 1, 1);

        expected = 366;
        days = PeriodUtil.daysBetween(start, end, PeriodStrategyEnum.CLOSE_CLOSE);
        assertEquals("same day count daysBetween: close close strategy fail", expected, days);

        expected = 365;
        days = PeriodUtil.daysBetween(start, end, PeriodStrategyEnum.CLOSE_OPEN);
        assertEquals("same day count daysBetween: close close strategy fail", expected, days);

        expected = 365;
        days = PeriodUtil.daysBetween(start, end, PeriodStrategyEnum.OPEN_CLOSE);
        assertEquals("same day count daysBetween: close close strategy fail", expected, days);

        expected = 364;
        days = PeriodUtil.daysBetween(start, end, PeriodStrategyEnum.OPEN_OPEN);
        assertEquals("same day count daysBetween: close close strategy fail", expected, days);
    }

    @Test
    public void negativeDaysBetween() {
        LocalDate start, end;
        int days, expected;

        //case 1
        start = LocalDate.of(2019, 1, 1);
        end = LocalDate.of(2018, 1, 1);

        expected = -366;
        days = PeriodUtil.daysBetween(start, end, PeriodStrategyEnum.CLOSE_CLOSE);
        assertEquals("same day count daysBetween: close close strategy fail", expected, days);

        expected = -365;
        days = PeriodUtil.daysBetween(start, end, PeriodStrategyEnum.CLOSE_OPEN);
        assertEquals("same day count daysBetween: close close strategy fail", expected, days);

        expected = -365;
        days = PeriodUtil.daysBetween(start, end, PeriodStrategyEnum.OPEN_CLOSE);
        assertEquals("same day count daysBetween: close close strategy fail", expected, days);

        expected = -364;
        days = PeriodUtil.daysBetween(start, end, PeriodStrategyEnum.OPEN_OPEN);
        assertEquals("same day count daysBetween: close close strategy fail", expected, days);
    }

    @Test
    public void sameDaysAfter() {
        LocalDate start, end, expected;

        int days = 0;
        start = LocalDate.of(2019, 1, 1);

        expected = LocalDate.of(2019, 1, 1);
        end = PeriodUtil.daysAfter(days, PeriodStrategyEnum.CLOSE_CLOSE, start);
        assertEquals("same day daysAfter: close close strategy fail", expected, end);

        expected = LocalDate.of(2019, 1, 1);
        end = PeriodUtil.daysAfter(days, PeriodStrategyEnum.CLOSE_OPEN, start);
        assertEquals("same day daysAfter: close open strategy fail", expected, end);

        expected = LocalDate.of(2019, 1, 1);
        end = PeriodUtil.daysAfter(days, PeriodStrategyEnum.OPEN_CLOSE, start);
        assertEquals("same day daysAfter: open close strategy fail", expected, end);

        expected = LocalDate.of(2019, 1, 1);
        end = PeriodUtil.daysAfter(days, PeriodStrategyEnum.OPEN_OPEN, start);
        assertEquals("same day daysAfter: open open strategy fail", expected, end);
    }

    @Test
    public void tomorrowDaysAfter() {
        LocalDate start, end, expected;

        int days = 1;
        start = LocalDate.of(2019, 1, 1);

        expected = LocalDate.of(2019, 1, 1);
        end = PeriodUtil.daysAfter(days, PeriodStrategyEnum.CLOSE_CLOSE, start);
        assertEquals("same day daysAfter: close close strategy fail", expected, end);

        expected = LocalDate.of(2019, 1, 2);
        end = PeriodUtil.daysAfter(days, PeriodStrategyEnum.CLOSE_OPEN, start);
        assertEquals("same day daysAfter: close open strategy fail", expected, end);

        expected = LocalDate.of(2019, 1, 2);
        end = PeriodUtil.daysAfter(days, PeriodStrategyEnum.OPEN_CLOSE, start);
        assertEquals("same day daysAfter: open close strategy fail", expected, end);

        expected = LocalDate.of(2019, 1, 3);
        end = PeriodUtil.daysAfter(days, PeriodStrategyEnum.OPEN_OPEN, start);
        assertEquals("same day daysAfter: open open strategy fail", expected, end);
    }

    @Test
    public void smallDaysAfter() {
        LocalDate start, end, expected;

        int days = 5;
        start = LocalDate.of(2019, 1, 1);

        expected = LocalDate.of(2019, 1, 5);
        end = PeriodUtil.daysAfter(days, PeriodStrategyEnum.CLOSE_CLOSE, start);
        assertEquals("same day daysAfter: close close strategy fail", expected, end);

        expected = LocalDate.of(2019, 1, 6);
        end = PeriodUtil.daysAfter(days, PeriodStrategyEnum.CLOSE_OPEN, start);
        assertEquals("same day daysAfter: close open strategy fail", expected, end);

        expected = LocalDate.of(2019, 1, 6);
        end = PeriodUtil.daysAfter(days, PeriodStrategyEnum.OPEN_CLOSE, start);
        assertEquals("same day daysAfter: open close strategy fail", expected, end);

        expected = LocalDate.of(2019, 1, 7);
        end = PeriodUtil.daysAfter(days, PeriodStrategyEnum.OPEN_OPEN, start);
        assertEquals("same day daysAfter: open open strategy fail", expected, end);
    }

    @Test
    public void bigDaysAfter() {
        LocalDate start, end, expected;

        int days = 40;
        start = LocalDate.of(2019, 1, 1);

        expected = LocalDate.of(2019, 2, 9);
        end = PeriodUtil.daysAfter(days, PeriodStrategyEnum.CLOSE_CLOSE, start);
        assertEquals("same day daysAfter: close close strategy fail", expected, end);

        expected = LocalDate.of(2019, 2, 10);
        end = PeriodUtil.daysAfter(days, PeriodStrategyEnum.CLOSE_OPEN, start);
        assertEquals("same day daysAfter: close open strategy fail", expected, end);

        expected = LocalDate.of(2019, 2, 10);
        end = PeriodUtil.daysAfter(days, PeriodStrategyEnum.OPEN_CLOSE, start);
        assertEquals("same day daysAfter: open close strategy fail", expected, end);

        expected = LocalDate.of(2019, 2, 11);
        end = PeriodUtil.daysAfter(days, PeriodStrategyEnum.OPEN_OPEN, start);
        assertEquals("same day daysAfter: open open strategy fail", expected, end);
    }

    @Test
    public void largeDaysAfter() {
        LocalDate start, end, expected;

        int days = 375;
        start = LocalDate.of(2018, 1, 1);

        expected = LocalDate.of(2019, 1, 10);
        end = PeriodUtil.daysAfter(days, PeriodStrategyEnum.CLOSE_CLOSE, start);
        assertEquals("same day daysAfter: close close strategy fail", expected, end);

        expected = LocalDate.of(2019, 1, 11);
        end = PeriodUtil.daysAfter(days, PeriodStrategyEnum.CLOSE_OPEN, start);
        assertEquals("same day daysAfter: close open strategy fail", expected, end);

        expected = LocalDate.of(2019, 1, 11);
        end = PeriodUtil.daysAfter(days, PeriodStrategyEnum.OPEN_CLOSE, start);
        assertEquals("same day daysAfter: open close strategy fail", expected, end);

        expected = LocalDate.of(2019, 1, 12);
        end = PeriodUtil.daysAfter(days, PeriodStrategyEnum.OPEN_OPEN, start);
        assertEquals("same day daysAfter: open open strategy fail", expected, end);
    }

    @Test
    public void negativeDaysAfter() {
        LocalDate start, end, expected;

        int days = -355;
        start = LocalDate.of(2019, 1, 1);

        expected = LocalDate.of(2018, 1, 12);
        end = PeriodUtil.daysAfter(days, PeriodStrategyEnum.CLOSE_CLOSE, start);
        assertEquals("negative day daysAfter: close close strategy fail", expected, end);

        expected = LocalDate.of(2018, 1, 11);
        end = PeriodUtil.daysAfter(days, PeriodStrategyEnum.CLOSE_OPEN, start);
        assertEquals("negative day daysAfter: close open strategy fail", expected, end);

        expected = LocalDate.of(2018, 1, 11);
        end = PeriodUtil.daysAfter(days, PeriodStrategyEnum.OPEN_CLOSE, start);
        assertEquals("negative day daysAfter: open close strategy fail", expected, end);

        expected = LocalDate.of(2018, 1, 10);
        end = PeriodUtil.daysAfter(days, PeriodStrategyEnum.OPEN_OPEN, start);
        assertEquals("negative day daysAfter: open open strategy fail", expected, end);
    }

    @Test
    public void yesterdayDaysAfter() {
        LocalDate start, end, expected;

        int days = -1;
        start = LocalDate.of(2019, 1, 3);

        expected = LocalDate.of(2019, 1, 3);
        end = PeriodUtil.daysAfter(days, PeriodStrategyEnum.CLOSE_CLOSE, start);
        assertEquals("negative day daysAfter: close close strategy fail", expected, end);

        expected = LocalDate.of(2019, 1, 2);
        end = PeriodUtil.daysAfter(days, PeriodStrategyEnum.CLOSE_OPEN, start);
        assertEquals("negative day daysAfter: close open strategy fail", expected, end);

        expected = LocalDate.of(2019, 1, 2);
        end = PeriodUtil.daysAfter(days, PeriodStrategyEnum.OPEN_CLOSE, start);
        assertEquals("negative day daysAfter: open close strategy fail", expected, end);

        expected = LocalDate.of(2019, 1, 1);
        end = PeriodUtil.daysAfter(days, PeriodStrategyEnum.OPEN_OPEN, start);
        assertEquals("negative day daysAfter: open open strategy fail", expected, end);
    }

    @Test
    public void twoDaysAgo() {
        LocalDate start, end, expected;

        int days = -2;
        start = LocalDate.of(2019, 1, 4);

        expected = LocalDate.of(2019, 1, 3);
        end = PeriodUtil.daysAfter(days, PeriodStrategyEnum.CLOSE_CLOSE, start);
        assertEquals("negative day daysAfter: close close strategy fail", expected, end);

        expected = LocalDate.of(2019, 1, 2);
        end = PeriodUtil.daysAfter(days, PeriodStrategyEnum.CLOSE_OPEN, start);
        assertEquals("negative day daysAfter: close open strategy fail", expected, end);

        expected = LocalDate.of(2019, 1, 2);
        end = PeriodUtil.daysAfter(days, PeriodStrategyEnum.OPEN_CLOSE, start);
        assertEquals("negative day daysAfter: open close strategy fail", expected, end);

        expected = LocalDate.of(2019, 1, 1);
        end = PeriodUtil.daysAfter(days, PeriodStrategyEnum.OPEN_OPEN, start);
        assertEquals("negative day daysAfter: open open strategy fail", expected, end);
    }

    @Test
    public void buildDateList() {
        LocalDateTime start = LocalDateTime.of(2021, 11, 2, 22, 13, 54);
        LocalDateTime end = LocalDateTime.of(2021, 11, 9, 8, 31, 29);

        List<LocalDateTime> result1 = PeriodUtil.buildDateList(start, end, true);
        System.out.println(result1);
        List<LocalDateTime> result2 = PeriodUtil.buildDateList(start, end, false);
        System.out.println(result2);
    }
}
