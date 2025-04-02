package cn.coderule.common.util.lang.time;

import cn.coderule.common.util.lang.StringUtil;
import java.time.temporal.ChronoUnit;
import lombok.NonNull;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Date;
import java.util.Locale;

/**
 * com.wolf.common.util
 *
 * @author Wingle
 * @since 2019/10/23 10:44 下午
 **/
public class DateUtil {
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final DateTimeFormatter DEFAULT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATETIME_FORMAT);
    public static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);

    public static Date asDate(@NonNull LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date asDate(@NonNull LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate asLocalDate(@NonNull Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDate asLocalDate(@NonNull String str) {
        return asLocalDate(str, null);
    }

    public static LocalDate asLocalDate(@NonNull String str, String format) {
        return asLocalDate(str, format, Locale.ENGLISH);
    }

    public static LocalDate asLocalDate(@NonNull String str, String format, @NonNull Locale locale) {
        DateTimeFormatter formatter;
        if (StringUtil.notBlank(format)) {
            formatter = DateTimeFormatter.ofPattern(format, locale);
        } else {
            formatter = DEFAULT_DATE_FORMATTER;
        }

        return LocalDate.parse(str, formatter);
    }

    public static LocalDateTime asLocalDateTime(@NonNull String str, String format) {
        return asLocalDateTime(str, format, Locale.ENGLISH);
    }

    public static LocalDateTime asLocalDateTime(@NonNull String str, String format, Locale locale) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format, locale);
        return LocalDateTime.parse(str, formatter);
    }

    public static LocalDateTime asLocalDateTime(@NonNull String str) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_DATETIME_FORMAT);
        return LocalDateTime.parse(str, formatter);
    }

    public static LocalDateTime asLocalDateTime(@NonNull Long ts) {
        return LocalDateTime.ofEpochSecond(ts, 0, ZoneOffset.UTC);
    }

    public static LocalDateTime asLocalDateTime(@NonNull Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDateTime asLocalDateTime(@NonNull LocalDate localDate) {
        return localDate.atStartOfDay();
    }

    public static LocalDateTime asLocalDateTimeCompatibleMode(@NonNull String str) {
        LocalDateTime result = null;
        try {
            // for "2007-12-03T10:15:30"
            result = LocalDateTime.parse(str);
        } catch (Exception ignored) {
        }
        if (result != null) {
            return result;
        }

        try {
            // for "2007-12-03 10:15:30.001"
            String tmpS = str;
            int tailIdx = tmpS.indexOf(".");
            if (tailIdx > 0) {
                tmpS = tmpS.substring(0, tailIdx);
            }
            // for "2007-12-03 10:15:30"
            result = DateUtil.asLocalDateTime(tmpS);
        } catch (Exception ignored) {
        }

        return result;
    }

    public static int asEpochSecond(LocalDateTime localDateTime) {
        long milli = asEpochMilli(localDateTime);
        milli = milli / 1000;
        return (int) milli;
    }

    public static Long asEpochMilli(@NonNull LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static Date secondsLater(int seconds) {
        LocalDateTime now = LocalDateTime.now();
        return DateUtil.asDate(now.plusSeconds(seconds));
    }

    public static String asString(@NonNull Date date) {
        return asString(date, DEFAULT_DATETIME_FORMAT);
    }

    public static String asString(@NonNull Date date, @NonNull String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String asString(@NonNull LocalDate localDate) {
        return asString(localDate, null);
    }

    public static String asString(@NonNull LocalDate localDate, String format) {
        if (null == format) {
            return localDate.format(DEFAULT_DATE_FORMATTER);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return localDate.format(formatter);
    }

    public static String asString(LocalDateTime localDateTime) {
        return asString(localDateTime, null);
    }

    public static String asString(LocalDateTime localDateTime, String format) {
        if (localDateTime == null) {
            return "";
        }

        if (format == null) {
            return localDateTime.format(DEFAULT_DATETIME_FORMATTER);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return localDateTime.format(formatter);
    }

    public static int getWeek(@NonNull LocalDateTime time) {
        WeekFields weekFields = WeekFields.ISO;
        return time.get(weekFields.weekOfWeekBasedYear());
    }

    public static int getWeek(@NonNull LocalDate time) {
        WeekFields weekFields = WeekFields.ISO;
        return time.get(weekFields.weekOfWeekBasedYear());
    }

    public static boolean isSameSecond(@NonNull LocalDateTime time) {
        return isSameSecond(time, LocalDateTime.now());
    }

    public static boolean isSameSecond(@NonNull LocalDateTime time, @NonNull LocalDateTime vsTime) {
        if (!isSameMinute(time, vsTime)) {
            return false;
        }

        return time.getSecond() == vsTime.getSecond();
    }

    public static boolean isSameMinute(@NonNull LocalDateTime time) {
        return isSameMinute(time, LocalDateTime.now());
    }

    public static boolean isSameMinute(@NonNull LocalDateTime time, @NonNull LocalDateTime vsTime) {
        if (!isSameHour(time, vsTime)) {
            return false;
        }

        return time.getMinute() == vsTime.getMinute();
    }

    public static boolean isSameHour(@NonNull LocalDateTime time) {
        return isSameHour(time, LocalDateTime.now());
    }

    public static boolean isSameHour(@NonNull LocalDateTime time, @NonNull LocalDateTime vsTime) {
        if (!isSameDay(time, vsTime)) {
            return false;
        }

        return time.getHour() == vsTime.getHour();
    }

    public static boolean isSameDay(@NonNull LocalDateTime time) {
        return isSameDay(time, LocalDateTime.now());
    }

    public static boolean isSameDay(@NonNull LocalDateTime time, @NonNull LocalDateTime vsTime) {
        if (time.getYear() != vsTime.getYear()) {
            return false;
        }

        if (time.getMonthValue() != vsTime.getMonthValue()) {
            return false;
        }

        return time.getDayOfMonth() == vsTime.getDayOfMonth();
    }

    public static boolean isSameWeek(@NonNull LocalDateTime time) {
        return isSameWeek(time, LocalDateTime.now());
    }

    public static boolean isSameWeek(@NonNull LocalDateTime time, @NonNull LocalDateTime vsTime) {
        if (time.getYear() != vsTime.getYear()) {
            return false;
        }

        return getWeek(time) == getWeek(vsTime);
    }

    public static boolean isSameMonth(@NonNull LocalDateTime time) {
        return isSameMonth(time, LocalDateTime.now());
    }

    public static boolean isSameMonth(@NonNull LocalDateTime time, @NonNull LocalDateTime vsTime) {
        if (time.getYear() != vsTime.getYear()) {
            return false;
        }

        return time.getMonthValue() == vsTime.getMonthValue();
    }

    public static boolean isSameYear(@NonNull LocalDateTime time) {
        return isSameYear(time, LocalDateTime.now());
    }

    public static boolean isSameYear(@NonNull LocalDateTime time, @NonNull LocalDateTime vsTime) {
        return time.getYear() == vsTime.getYear();
    }

    public static boolean isSameDay(@NonNull LocalDate time) {
        return isSameDay(time, LocalDate.now());
    }

    public static boolean isSameDay(@NonNull LocalDate time, @NonNull LocalDate vsTime) {
        if (time.getYear() != vsTime.getYear()) {
            return false;
        }

        if (time.getMonthValue() != vsTime.getMonthValue()) {
            return false;
        }

        return time.getDayOfMonth() == vsTime.getDayOfMonth();
    }

    public static boolean isSameWeek(@NonNull LocalDate time) {
        return isSameWeek(time, LocalDate.now());
    }

    public static boolean isSameWeek(@NonNull LocalDate time, @NonNull LocalDate vsTime) {
        if (time.getYear() != vsTime.getYear()) {
            return false;
        }

        return getWeek(time) == getWeek(vsTime);
    }

    public static boolean isSameMonth(@NonNull LocalDate time) {
        return isSameMonth(time, LocalDate.now());
    }

    public static boolean isSameMonth(@NonNull LocalDate time, @NonNull LocalDate vsTime) {
        if (time.getYear() != vsTime.getYear()) {
            return false;
        }

        return time.getMonthValue() == vsTime.getMonthValue();
    }

    public static boolean isSameYear(@NonNull LocalDate time) {
        return isSameYear(time, LocalDate.now());
    }

    public static boolean isSameYear(@NonNull LocalDate time, @NonNull LocalDate vsTime) {
        return time.getYear() == vsTime.getYear();
    }

    public static boolean isDaysBefore(@NonNull LocalDate time, int days) {
        return isDaysBefore(time, days, LocalDate.now());

    }

    public static boolean isDaysBefore(@NonNull LocalDate time, int days, @NonNull LocalDate vsTime) {
        LocalDate daysBefore = vsTime.minusDays(days);
        return !daysBefore.isBefore(time);
    }

    public static boolean isDaysAfter(@NonNull LocalDate time, int days) {
        return isDaysAfter(time, days, LocalDate.now());

    }

    public static boolean isDaysAfter(@NonNull LocalDate time, int days, @NonNull LocalDate vsTime) {
        LocalDate daysAfter = vsTime.plusDays(days);
        return !daysAfter.isAfter(time);
    }

    public static int weekOfMonth(@NonNull LocalDate date) {
        WeekFields weekFields = WeekFields.of(DayOfWeek.SUNDAY, 1);
        TemporalField weekOfMonth = weekFields.weekOfMonth();
        return date.get(weekOfMonth);
    }

    public static LocalDateTime setTimeToStartOfDay(@NonNull LocalDateTime time) {
        LocalDate date = time.toLocalDate();
        return date.atStartOfDay();
    }

    public static LocalDateTime setTimeToEndOfDay(@NonNull LocalDateTime time) {
        LocalDate date = time.toLocalDate();
        return LocalDateTime.of(date, LocalTime.of(23, 59, 59));
    }

    public static boolean isWeekend(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    public static boolean isAtWeekend(LocalDateTime dateTime) {
        DayOfWeek dayOfWeek = dateTime.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    public static long daysBetween(@NonNull LocalDate start, @NonNull LocalDate end) {
        return ChronoUnit.DAYS.between(start, end);
    }
}
