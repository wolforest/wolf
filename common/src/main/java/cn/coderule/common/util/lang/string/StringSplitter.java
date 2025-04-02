package cn.coderule.common.util.lang.string;

import com.google.common.base.Splitter;

import java.util.regex.Pattern;

/**
 * wrap of guava Splitter
 */
public class StringSplitter {
    public static Splitter on(char separator) {
        return Splitter.on(separator);
    }

    public static Splitter on(String separator) {
        return Splitter.on(separator);
    }

    public static Splitter on(Pattern separator) {
        return Splitter.on(separator);
    }
}
