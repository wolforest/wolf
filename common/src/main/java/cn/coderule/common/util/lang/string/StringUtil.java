package cn.coderule.common.util.lang.string;

import com.google.common.base.CaseFormat;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.html.HtmlEscapers;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import cn.coderule.common.util.lang.collection.CollectionUtil;
import cn.coderule.common.util.lang.collection.MapUtil;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * com.wolf.common.util
 *
 * @author Wingle
 * @since 2019/12/10 9:51 上午
 **/
public class StringUtil {
    public static final String DEFAULT_DELIMITER = "";
    public static final String EMPTY = "";
    public static final String ZERO = "0";
    public static final String BLANK = " ";
    public static final String COMMA = ",";
    public static final String COLON = ":";
    public static final String QUOTE = "`";
    public static final String UNDERSCORE = "_";
    private static final String EMAIL_REGEX_PATTERN_RFC5322 = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    private final static char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();


    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.isEmpty();
    }

    public static boolean notEmpty(CharSequence s) {
        return !isEmpty(s);
    }

    public static boolean isAllEmpty(final CharSequence... css) {
        return StringUtils.isAllEmpty(css);
    }

    public static boolean isAnyEmpty(final CharSequence... css) {
        return StringUtils.isAnyEmpty(css);
    }

    public static boolean isNoneEmpty(final CharSequence... css) {
        return !isAnyEmpty(css);
    }

    public static boolean isAllBlank(final CharSequence... css) {
        return StringUtils.isAllBlank(css);
    }

    public static boolean isAnyBlank(final CharSequence... css) {
        return StringUtils.isAnyBlank(css);
    }

    public static boolean isNoneBlank(final CharSequence... css) {
        return !isAnyBlank(css);
    }

    public static boolean notBlank(Object s) {
        return !isBlank(s);
    }

    public static boolean isBlank(Object s) {
        if (!(s instanceof CharSequence)) {
            return true;
        }

        return isBlank((CharSequence) s);
    }

    public static boolean isBlank(CharSequence s) {
        return StringUtils.isBlank(s);
    }

    public static boolean notBlank(String s) {
        return !isBlank(s);
    }


    public static boolean isZero(String s) {
        return isZero(s, false);
    }

    public static boolean isZero(String s, boolean trim) {
        if (isBlank(s)) {
            return false;
        }

        if (ZERO.equalsIgnoreCase(s)) {
            return true;
        }

        String target = trim ? s.trim() : s;
        for (int i = 0, l = target.length(); i < l; i++) {
            char c = target.charAt(i);
            if (c != '0') {
                return false;
            }
        }

        return true;
    }

    public static boolean isTrue(String s) {
        return "true".equalsIgnoreCase(s);
    }

    public static boolean isFalse(String s) {
        return "false".equalsIgnoreCase(s);
    }

    public static boolean containsSpace(String s) {
        return s != null && s.indexOf(' ') != -1;
    }

    public static String join(Object f, @Nullable Object s, Object... r) {
        return joinWith(DEFAULT_DELIMITER, f, s, r);
    }

    public static String joinWith(String delimiter, Iterable<?> parts) {
        if (delimiter == null) {
            delimiter = DEFAULT_DELIMITER;
        }

        Joiner joiner = Joiner.on(delimiter).skipNulls();
        return joiner.join(parts);
    }

    public static String joinWith(String delimiter, Object f, @Nullable Object s, Object... r) {
        if (delimiter == null) {
            delimiter = DEFAULT_DELIMITER;
        }

        Joiner joiner = Joiner.on(delimiter).skipNulls();
        return joiner.join(f, s, r);
    }

    public static String joinSkipBlank(String delimiter, Object... r) {
        StringBuilder sb = new StringBuilder();
        String s;
        boolean isFirst = true;
        for (Object o : r) {
            s = o.toString();
            if (isBlank(s)) {
                continue;
            }

            if (!isFirst) {
                sb.append(delimiter);
            }

            sb.append(s);
            isFirst = false;
        }

        return sb.toString();
    }

    public static String quote(String keyword) {
        return quote(keyword, false);
    }

    public static String quote(String keyword, boolean isColumnsString) {
        if ("*".equals(keyword.trim())) {
            return keyword;
        }

        if (!isColumnsString) {
            return join(QUOTE, keyword.trim(), QUOTE);
        }

        String[] keywords = keyword.split(COMMA);
        StringBuilder result = new StringBuilder();

        boolean isFirst = true;
        for (String s : keywords) {
            if (!isFirst) {
                result.append(COMMA);
            }
            isFirst = false;

            result.append(QUOTE).append(s.trim()).append(QUOTE);
        }

        return result.toString();
    }

    public static String trim(String s) {
        return StringUtils.trim(s);
    }

    public static String trimToNull(String s) {
        return StringUtils.trimToNull(s);
    }

    public static String trimToEmpty(String s) {
        return StringUtils.trimToEmpty(s);
    }

    public static String truncate(final String str, final int maxWidth) {
        return truncate(str, 0, maxWidth);
    }

    public static String truncate(final String str, final int offset, final int maxWidth) {
        return StringUtils.truncate(str, offset, maxWidth);
    }

    public static boolean isNumeric(String s) {
        return StringUtils.isNumeric(s);
    }

    public static String strip(final String str, final String stripChars) {
        return StringUtils.strip(str, stripChars);
    }

    public static String strip(String s) {
        return StringUtils.strip(s);
    }

    public static String stripToNull(String s) {
        return StringUtils.stripToNull(s);
    }

    public static String stripStart(final String str, final String stripChars) {
        return StringUtils.stripStart(str, stripChars);
    }

    public static String stripEnd(final String str, final String stripChars) {
        return StringUtils.stripEnd(str, stripChars);
    }

    public static String[] stripAll(final String... strs) {
        return stripAll(strs, null);
    }

    public static boolean equals(final CharSequence cs1, final CharSequence cs2) {
        return StringUtils.equals(cs1, cs2);
    }

    public static boolean equalsIgnoreCase(final CharSequence cs1, final CharSequence cs2) {
        return StringUtils.equalsIgnoreCase(cs1, cs2);
    }

    public static String[] stripAll(final String[] strs, final String stripChars) {
        return StringUtils.stripAll(strs, stripChars);
    }

    public static int compare(final String str1, final String str2) {
        return compare(str1, str2, true);
    }

    public static int compare(final String str1, final String str2, final boolean nullIsLess) {
        return StringUtils.compare(str1, str2, nullIsLess);
    }

    public static int compareIgnoreCase(final String str1, final String str2) {
        return compareIgnoreCase(str1, str2, true);
    }

    public static int compareIgnoreCase(final String str1, final String str2, final boolean nullIsLess) {
        return StringUtils.compareIgnoreCase(str1, str2, nullIsLess);
    }

    public static String ltrim(String s, String prefix) {
        int prefixLen = prefix.length();
        if (0 == prefixLen) {
            return s;
        }

        String sPrefix = s.substring(0, prefixLen);
        if (!sPrefix.equals(prefix)) {
            return s;
        }

        return s.substring(prefixLen);
    }

    public static String rtrim(String s, String suffix) {
        int suffixLen = suffix.length();
        if (0 == suffixLen) {
            return s;
        }

        int sLen = s.length();
        String sSuffix = s.substring(sLen - suffixLen);
        if (!sSuffix.equals(suffix)) {
            return s;
        }

        return s.substring(0, sLen - suffixLen);
    }

    public static String substringFrom(String s, String separator) {
        return substringFrom(s, separator, false);
    }

    public static String substringFrom(String s, String separator, boolean originForNone) {
        if (s == null || null == separator) {
            throw new IllegalArgumentException("null args for substringFrom");
        }

        int pos = s.indexOf(separator);
        if (-1 == pos) {
            return originForNone ? s : "";
        }

        return s.substring(pos + separator.length());
    }

    public static String substringLastFrom(String s, String separator) {
        return substringLastFrom(s, separator, false);
    }

    public static String substringLastFrom(String s, String separator, boolean originForNone) {
        if (s == null || null == separator) {
            throw new IllegalArgumentException("null args for substringFrom");
        }

        int pos = s.lastIndexOf(separator);
        if (-1 == pos) {
            return originForNone ? s : "";
        }

        return s.substring(pos + separator.length());
    }

    public static String substringTo(String s, String separator) {
        return substringTo(s, separator, false);
    }

    public static String substringTo(String s, String separator, boolean originForNone) {
        if (s == null || null == separator) {
            throw new IllegalArgumentException("null args for substringTo");
        }

        int pos = s.indexOf(separator);
        if (-1 == pos) {
            return originForNone ? s : "";
        }

        return s.substring(0, pos);
    }

    public static String substringLastTo(String s, String separator) {
        return substringLastTo(s, separator, false);
    }

    public static String substringLastTo(String s, String separator, boolean originForNone) {
        if (s == null || null == separator) {
            throw new IllegalArgumentException("null args for substringTo");
        }

        int pos = s.lastIndexOf(separator);
        if (-1 == pos) {
            return originForNone ? s : "";
        }

        return s.substring(0, pos);
    }

    public static String substringToBlank(String s, int len) {
        if (isBlank(s) || s.length() <= len) {
            return s;
        }

        int blankPos = s.indexOf(BLANK, len);
        if (blankPos <= 0) {
            return s.substring(0, len);
        }

        return s.substring(0, blankPos);
    }

    public static String substring(String s, int start) {
        return StringUtils.substring(s, start);
    }

    public static String substring(String str, int start, int end) {
        return StringUtils.substring(str, start, end);
    }

    public static String camel(String s) {
        return lowerCamel(s);
    }

    public static String camel(String s, String separator) {
        return lowerCamel(s, separator);
    }

    public static String lowerCamel(String s, String separator) {
        if (null == separator || separator.equals(UNDERSCORE)) {
            return lowerCamel(s);
        }
        return lowerCamel(s.replace(separator, UNDERSCORE));
    }

    public static String lowerCamel(String s) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, s);
    }

    public static String upperCamel(String s, String separator) {
        if (null == separator || separator.equals(UNDERSCORE)) {
            return upperCamel(s);
        }
        return upperCamel(s.replace(separator, UNDERSCORE));
    }

    public static String upperCamel(String s) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, s);
    }

    public static String camelTo(String s) {
        return camelTo(s, null);
    }

    public static String camelTo(String s, String separator) {
        String result = underscore(s);
        if (null == separator || separator.equals(UNDERSCORE)) {
            return result;
        }

        return result.replace(UNDERSCORE, separator);
    }

    public static String underscore(String s) {
        return lowerUnderscore(s);
    }

    public static String lowerUnderscore(String s) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, s);
    }

    public static String upperUnderscore(String s) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, s);
    }

    public static String capitalize(String str) {
        if (str == null) {
            return null;
        }

        char[] ch = str.toCharArray();
        if (0 == ch.length) {
            return str;
        }

        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        } else {
            return str;
        }

        return new String(ch);
    }

    public static String uncapitalize(String str) {
        if (str == null) {
            return null;
        }

        char[] ch = str.toCharArray();
        if (0 == ch.length) {
            return str;
        }

        if (ch[0] >= 'A' && ch[0] <= 'Z') {
            ch[0] = (char) (ch[0] + 32);
        } else {
            return str;
        }

        return new String(ch);
    }

    public static String ucWords(String s) {
        return ucWords(s, BLANK);
    }

    public static String ucWords(String s, String separator) {
        if (isBlank(s)) {
            return s;
        }

        String[] sArray = split(s, separator);
        for (int i = 0, len = sArray.length; i < len; i++) {
            sArray[i] = capitalize(sArray[i]);
        }

        return String.join(separator, sArray);
    }

    public static String lcWords(String s) {
        return lcWords(s, BLANK);
    }

    public static String lcWords(String s, String separator) {
        if (isBlank(s)) {
            return s;
        }

        String[] sArray = split(s, separator);
        for (int i = 0, len = sArray.length; i < len; i++) {
            if (isEmpty(sArray[i])) {
                continue;
            }
            sArray[i] = uncapitalize(sArray[i]);
        }

        return String.join(separator, sArray);
    }

    public static String addSlash(String s) {
        return s;
    }

    public static String[] split(String s, String separator) {
        return split(s, separator, false);
    }

    public static String[] split(String s, String separator, boolean trim) {
        if (isEmpty(separator)) {
            return new String[]{s};
        }

        Iterable<String> i = Splitter.on(separator).split(s);
        List<String> list = new ArrayList<String>();
        for (String item : i) {
            if (trim && StringUtil.notBlank(item)) {
                list.add(item.trim());
            } else {
                list.add(item);
            }
        }
        return list.toArray(new String[0]);
    }

    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String remove(String s, Collection<String> subs) {
        if (CollectionUtil.isEmpty(subs)) {
            throw new IllegalArgumentException("StringUtil.remove subs can't be empty");
        }

        for (String sub : subs) {
            s = s.replaceAll(StringUtil.join("(?i)", sub), "");
        }

        return s;
    }

    public static String remove(String s, String... subs) {
        if (subs.length == 0) {
            throw new IllegalArgumentException("StringUtil.remove subs can't be empty");
        }

        return remove(s, Arrays.asList(subs));
    }

    public static String replace(String s, Map<String, String> map) {
        if (MapUtil.isEmpty(map)) {
            return s;
        }

        String key;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            key = StringUtil.join("(?i)", entry.getKey());
            s = s.replaceAll(key, entry.getValue());
        }

        return s;
    }

    public static boolean startsWith(@NonNull String s, @NonNull String separator) {
        return startsWith(s, separator, false);
    }

    public static boolean startsWith(@NonNull String s, @NonNull String separator, boolean trim) {
        String target = s;
        if (trim) {
            target = s.trim();
        }

        return target.startsWith(separator);
    }

    public static boolean contains(String s, @NonNull String sub) {
        if (StringUtil.isBlank(s)) {
            return false;
        }

        return s.contains(sub);
    }

    public static boolean endsWith(@NonNull String s, @NonNull String separator) {
        return endsWith(s, separator, false);
    }

    public static boolean endsWith(@NonNull String s, @NonNull String separator, boolean trim) {
        String target = s;
        if (trim) {
            target = s.trim();
        }

        return target.endsWith(separator);
    }

    public static String leftPad(String str, int size, String padStr) {
        return StringUtils.leftPad(str, size, padStr);
    }

    public static String rightPad(String str, int size, String padStr) {
        return StringUtils.rightPad(str, size, padStr);
    }

    public static String format(String format, Object... args) {
        return String.format(format, args);
    }

    public static String mask(String str) {
        return mask(str, '*', 4);
    }

    public static String mask(String str, char maskChar, int maskLength) {
        if (null == str || isEmpty(str)) {
            return "";
        }

        StringBuilder mask = new StringBuilder(maskLength);
        mask.append(String.valueOf(maskChar).repeat(Math.max(0, maskLength + 1)));

        int len = str.length();
        if (len <= maskLength) {
            return join(str.substring(0, 1), mask.toString());
        }

        int head = (len % maskLength) / 2 <= 1 ? 1 : 2;
        int tail = str.length() - head;

        return join(str.substring(0, head), mask.toString(), str.substring(tail));
    }

    public static boolean match(String s, String pattern) {
        return Pattern.compile(pattern)
                .matcher(s)
                .matches();
    }

    public static boolean validateEmail(String email) {
        if (StringUtil.isBlank(email)) {
            return false;
        }

        return Pattern.compile(EMAIL_REGEX_PATTERN_RFC5322)
                .matcher(email)
                .matches();
    }

    public static String maskEmail(String email) {
        if (!validateEmail(email)) {
            return email;
        }

        String[] arr = email.split("@");
        if (arr.length != 2) {
            return email;
        }

        String maskedAccount = mask(arr[0]);
        return StringUtil.joinWith("@", maskedAccount, arr[1]);
    }

    public static String escapeHtml(String html) {
        if (isBlank(html)) {
            return html;
        }
        return HtmlEscapers.htmlEscaper().escape(html);
    }

    public static String escapeCsvSpecialCharacters(String data) {
        if (isBlank(data)) {
            return data;
        }

        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }

    public static String repeat(String base, int n) {
        if (n < 1) {
            return base;
        }

        return base + base.repeat(n - 1);
    }

    public static int countMatches(CharSequence str, CharSequence sub) {
        return StringUtils.countMatches(str, sub);
    }

    public static String exceptionToString(final Throwable e) {
        StringBuilder sb = new StringBuilder();
        if (e != null) {
            sb.append(e);

            StackTraceElement[] stackTrace = e.getStackTrace();
            if (stackTrace != null && stackTrace.length > 0) {
                StackTraceElement element = stackTrace[0];
                sb.append(", ");
                sb.append(element.toString());
            }
        }
        return sb.toString();
    }

    public static boolean isAscii(byte[] subject) {
        if (subject == null) {
            return false;
        }
        for (byte b : subject) {
            if (b < 32 || b > 126) {
                return false;
            }
        }
        return true;
    }

    public static <T extends CharSequence> T defaultIfBlank(T str, T defaultStr) {
        return isBlank(str) ? defaultStr : str;
    }

    public static <T extends CharSequence> T defaultIfEmpty(T str, T defaultStr) {
        return StringUtils.defaultIfEmpty(str, defaultStr);
    }

    public static String defaultString(String str) {
        return Objects.toString(str, "");
    }

    public static void writeInt(char[] buffer, int pos, int value) {
        for (int moveBits = 28; moveBits >= 0; moveBits -= 4) {
            buffer[pos++] = HEX_ARRAY[(value >>> moveBits) & 0x0F];
        }
    }

    public static void writeShort(char[] buffer, int pos, int value) {
        for (int moveBits = 12; moveBits >= 0; moveBits -= 4) {
            buffer[pos++] = HEX_ARRAY[(value >>> moveBits) & 0x0F];
        }
    }

    public static String bytes2string(byte[] src) {
        char[] hexChars = new char[src.length * 2];
        for (int j = 0; j < src.length; j++) {
            int v = src[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] string2bytes(String hexString) {
        if (hexString == null || hexString.isEmpty()) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }


}
