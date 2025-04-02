package cn.coderule.common.util.lang.string;

import com.google.common.base.Joiner;

/**
 * wrap of guava Joiner
 */
public class StringJoiner {
    public static Joiner on(String separator) {
        return Joiner.on(separator);
    }

    public static Joiner on(char separator) {
        return Joiner.on(separator);
    }

}
