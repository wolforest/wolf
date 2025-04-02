package cn.coderule.common.util.lang;

import java.util.concurrent.ThreadLocalRandom;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * com.wolf.common.util.lang
 *
 * @author Wingle
 * @since 2020/7/13 9:15 上午
 **/
public class RandomUtil {
    public static String randomNumeric(int length) {
        return RandomStringUtils.randomNumeric(length);
    }

    public static long randomLong(long max) {
        return randomLong(0, max);
    }
    public static long randomLong(long min, long max) {
        return ThreadLocalRandom.current().nextLong(min, max);
    }

    public static int randomInt(int max) {
        return randomInt(0, max);
    }
    public static int randomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }
}
