package cn.coderule.common.util.time;

import cn.coderule.common.util.lang.time.DateUtil;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.junit.Assert.*;

/**
 * com.wolf.common.util
 *
 * @author Wingle
 * @since 2019/10/23 10:54 下午
 **/
public class DateUtilTest {

    @Test
    public void test_is_same_hour_work_fine() {
        LocalDateTime t1, t2;

        t1 = LocalDateTime.of(2020, 1, 2, 10, 20, 10);
        t2 = LocalDateTime.of(2020, 1, 2, 10, 20, 10);
        assertTrue(DateUtil.isSameHour(t1, t2));

        t1 = LocalDateTime.of(2020, 1, 2, 10, 20, 10);
        t2 = LocalDateTime.of(2020, 1, 3, 10, 20, 10);
        assertFalse(DateUtil.isSameHour(t1, t2));

        t1 = LocalDateTime.of(2020, 1, 2, 10, 20, 10);
        t2 = LocalDateTime.of(2020, 1, 2, 11, 20, 10);
        assertFalse(DateUtil.isSameHour(t1, t2));
    }

    @Test
    public void test_is_same_minute_work_fine() {
        LocalDateTime t1, t2;

        t1 = LocalDateTime.of(2020, 1, 2, 10, 20, 10);
        t2 = LocalDateTime.of(2020, 1, 2, 11, 20, 11);
        assertFalse(DateUtil.isSameMinute(t1, t2));

        t1 = LocalDateTime.of(2020, 1, 2, 10, 20, 10);
        t2 = LocalDateTime.of(2020, 1, 2, 10, 20, 11);
        assertTrue(DateUtil.isSameMinute(t1, t2));

        t1 = LocalDateTime.of(2020, 1, 2, 10, 20, 11);
        t2 = LocalDateTime.of(2020, 1, 2, 10, 21, 10);
        assertFalse(DateUtil.isSameMinute(t1, t2));

        t1 = LocalDateTime.of(2020, 1, 2, 10, 20, 11);
        t2 = LocalDateTime.of(2020, 1, 2, 10, 21, 10);
        assertFalse(DateUtil.isSameMinute(t1, t2));
    }

    @Test
    public void test_is_same_second_work_fine() {
        LocalDateTime t1, t2;

        t1 = LocalDateTime.of(2020, 1, 2, 10, 20, 10);
        t2 = LocalDateTime.of(2020, 1, 2, 10, 21, 10);
        assertFalse(DateUtil.isSameSecond(t1, t2));

        t1 = LocalDateTime.of(2020, 1, 2, 10, 20, 10);
        t2 = LocalDateTime.of(2020, 1, 2, 10, 20, 10);
        assertTrue(DateUtil.isSameSecond(t1, t2));

        t1 = LocalDateTime.of(2020, 1, 2, 10, 20, 10);
        t2 = LocalDateTime.of(2020, 1, 2, 10, 20, 11);
        assertFalse(DateUtil.isSameSecond(t1, t2));
    }

    @Test
    public void test_is_same_day_work_fine() {
        LocalDateTime t1, t2;

        t1 = LocalDateTime.of(2020, 1, 2, 10, 20);
        t2 = LocalDateTime.of(2020, 1, 2, 3, 20);
        assertTrue(DateUtil.isSameDay(t1, t2));

        t1 = LocalDateTime.of(2020, 1, 2, 10, 20);
        t2 = LocalDateTime.of(2020, 1, 2, 23, 20);
        assertTrue(DateUtil.isSameDay(t1, t2));

        t1 = LocalDateTime.of(2020, 1, 2, 10, 20);
        t2 = LocalDateTime.of(2020, 1, 3, 23, 20);
        assertFalse(DateUtil.isSameDay(t1, t2));

        t1 = LocalDateTime.of(2020, 1, 2, 10, 20);
        t2 = LocalDateTime.of(2020, 2, 2, 23, 20);
        assertFalse(DateUtil.isSameDay(t1, t2));

        t1 = LocalDateTime.of(2020, 1, 2, 10, 20);
        t2 = LocalDateTime.of(2012, 1, 2, 23, 20);
        assertFalse(DateUtil.isSameDay(t1, t2));
    }

    @Test
    public void test_is_same_week_work_fine() {
        LocalDateTime t1, t2;

        t1 = LocalDateTime.of(2019, 12, 28, 10, 20);
        t2 = LocalDateTime.of(2020, 1, 1, 3, 20);
        assertFalse(DateUtil.isSameWeek(t1, t2));

        t1 = LocalDateTime.of(2019, 12, 30, 10, 20);
        t2 = LocalDateTime.of(2020, 1, 1, 3, 20);
        assertFalse(DateUtil.isSameWeek(t1, t2));

        t1 = LocalDateTime.of(2020, 1, 2, 10, 20);
        t2 = LocalDateTime.of(2020, 1, 2, 3, 20);
        assertTrue(DateUtil.isSameWeek(t1, t2));

        t1 = LocalDateTime.of(2020, 1, 2, 10, 20);
        t2 = LocalDateTime.of(2020, 1, 3, 23, 20);
        assertTrue(DateUtil.isSameWeek(t1, t2));

        t1 = LocalDateTime.of(2020, 1, 2, 10, 20);
        t2 = LocalDateTime.of(2020, 1, 5, 23, 20);
        assertTrue(DateUtil.isSameWeek(t1, t2));

        t1 = LocalDateTime.of(2020, 1, 2, 10, 20);
        t2 = LocalDateTime.of(2020, 1, 6, 23, 20);
        assertFalse(DateUtil.isSameWeek(t1, t2));

        t1 = LocalDateTime.of(2020, 1, 2, 10, 20);
        t2 = LocalDateTime.of(2020, 2, 2, 23, 20);
        assertFalse(DateUtil.isSameWeek(t1, t2));

        t1 = LocalDateTime.of(2020, 1, 2, 10, 20);
        t2 = LocalDateTime.of(2012, 1, 2, 23, 20);
        assertFalse(DateUtil.isSameWeek(t1, t2));


        t1 = LocalDateTime.of(2020, 1, 30, 10, 20);
        t2 = LocalDateTime.of(2020, 2, 1, 23, 20);
        assertTrue(DateUtil.isSameWeek(t1, t2));

        t1 = LocalDateTime.of(2020, 1, 30, 10, 20);
        t2 = LocalDateTime.of(2020, 2, 2, 23, 20);
        assertTrue(DateUtil.isSameWeek(t1, t2));

        t1 = LocalDateTime.of(2020, 1, 30, 10, 20);
        t2 = LocalDateTime.of(2020, 2, 3, 23, 20);
        assertFalse(DateUtil.isSameWeek(t1, t2));
    }

    @Test
    public void asDate() {
    }

    @Test
    public void testAsDate() {
    }

    @Test
    public void asLocalDate() {
    }

    @Test
    public void testAsLocalDate() {
    }

    @Test
    public void testAsLocalDate1() {
    }

    @Test
    public void testAsLocalDate2() {
    }

    @Test
    public void asLocalDateTime() {
    }

    @Test
    public void testAsLocalDateTime() {
    }

    @Test
    public void testAsLocalDateTime1() {
    }

    @Test
    public void testAsLocalDateTime2() {
    }

    @Test
    public void testAsLocalDateTime3() {
    }

    @Test
    public void asEpochSecond() {
    }

    @Test
    public void asEpochMilli() {
    }

    @Test
    public void secondsLater() {
    }

    @Test
    public void asString() {
    }

    @Test
    public void testAsString() {
    }

    @Test
    public void testAsString1() {
    }

    @Test
    public void testAsString2() {
    }

    @Test
    public void testAsString3() {
    }

    @Test
    public void testAsString4() {
    }

    @Test
    public void getWeek() {
    }

    @Test
    public void testGetWeek() {
    }

    @Test
    public void isSameSecond() {
    }

    @Test
    public void testIsSameSecond() {
    }

    @Test
    public void isSameMinute() {
    }

    @Test
    public void testIsSameMinute() {
    }

    @Test
    public void isSameHour() {
    }

    @Test
    public void testIsSameHour() {
    }

    @Test
    public void isSameDay() {
    }

    @Test
    public void testIsSameDay() {
    }

    @Test
    public void isSameWeek() {
    }

    @Test
    public void testIsSameWeek() {
    }

    @Test
    public void isSameMonth() {
    }

    @Test
    public void testIsSameMonth() {
    }

    @Test
    public void isSameYear() {
    }

    @Test
    public void testIsSameYear() {
    }

    @Test
    public void testIsSameDay1() {
    }

    @Test
    public void testIsSameDay2() {
    }

    @Test
    public void testIsSameWeek1() {
    }

    @Test
    public void testIsSameWeek2() {
    }

    @Test
    public void testIsSameMonth1() {
    }

    @Test
    public void testIsSameMonth2() {
    }

    @Test
    public void testIsSameYear1() {
    }

    @Test
    public void testIsSameYear2() {
    }

    @Test
    public void isDaysBefore() {
    }

    @Test
    public void testIsDaysBefore() {
        LocalDate t1, t2;

        t1 = LocalDate.of(2021, 10, 27);
        t2 = LocalDate.of(2021, 10, 20);
        assertTrue("DateUtil.isDaysBefore failed", DateUtil.isDaysBefore(t2, 7, t1));

        t1 = LocalDate.of(2021, 10, 27);
        t2 = LocalDate.of(2021, 10, 19);
        assertTrue("DateUtil.isDaysBefore failed", DateUtil.isDaysBefore(t2, 7, t1));


        t1 = LocalDate.of(2021, 10, 27);
        t2 = LocalDate.of(2021, 10, 21);
        assertFalse("DateUtil.isDaysBefore failed", DateUtil.isDaysBefore(t2, 7, t1));
    }

    @Test
    public void isDaysAfter() {
    }

    @Test
    public void testIsDaysAfter() {
        LocalDate t1, t2;

        t1 = LocalDate.of(2021, 10, 27);
        t2 = LocalDate.of(2021, 10, 20);
        assertTrue("DateUtil.isDaysAfter failed", DateUtil.isDaysAfter(t1, 7, t2));

        t1 = LocalDate.of(2021, 10, 27);
        t2 = LocalDate.of(2021, 10, 19);
        assertTrue("DateUtil.isDaysAfter failed", DateUtil.isDaysAfter(t1, 7, t2));


        t1 = LocalDate.of(2021, 10, 27);
        t2 = LocalDate.of(2021, 10, 21);
        assertFalse("DateUtil.isDaysAfter failed", DateUtil.isDaysAfter(t1, 7, t2));
    }

    @Test
    public void testIsWeekendByLocalDate() {
        LocalDate t1, t2, t3;
        t1 = LocalDate.of(2022, 3, 23);
        t2 = LocalDate.of(2022, 3, 26);
        t3 = LocalDate.of(2022, 3, 27);

        assertFalse("test DateUtil.isWeekDay failed", DateUtil.isWeekend(t1));
        assertTrue("test DateUtil.isWeekDay failed", DateUtil.isWeekend(t2));
        assertTrue("test DateUtil.isWeekDay failed", DateUtil.isWeekend(t3));
    }

    @Test
    public void testIsWeekendByLocalDateTime() {
        LocalDateTime t1, t2, t3;
        t1 = LocalDateTime.of(2022, 3, 23, 11, 15, 55);
        t2 = LocalDateTime.of(2022, 3, 26, 11, 15, 55);
        t3 = LocalDateTime.of(2022, 3, 27, 11, 15, 55);

        assertFalse("test DateUtil.isWeekDay failed", DateUtil.isAtWeekend(t1));
        assertTrue("test DateUtil.isWeekDay failed", DateUtil.isAtWeekend(t2));
        assertTrue("test DateUtil.isWeekDay failed", DateUtil.isAtWeekend(t3));
    }

    @Test
    public void asLocalDateTimeCompatibleMode() {
    }

    @Test
    public void daysBetween() {
        LocalDate start, end;

        start = LocalDate.of(2024, 1, 1);
        end = LocalDate.of(2024, 1, 2);
        assertEquals("test DateUtil.daysBetween failed", 1, DateUtil.daysBetween(start, end));
        assertEquals("test DateUtil.daysBetween failed", -1, DateUtil.daysBetween(end, start));
    }
}
