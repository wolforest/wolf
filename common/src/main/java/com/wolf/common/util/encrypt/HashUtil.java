package com.wolf.common.util.encrypt;

import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;
import lombok.NonNull;

/**
 * com.wolf.common.util.encrypt
 *
 * @author Wingle
 * @since 2020/3/16 8:28 下午
 **/
public class HashUtil {
    @SuppressWarnings("ALL")
    public static String md5(@NonNull String str) {
        return Hashing.md5().hashUnencodedChars(str).toString();
    }

    public static String crc32(@NonNull String str) {
        return Hashing.crc32().hashBytes(str.getBytes(StandardCharsets.UTF_8)).toString();
    }

    public static String murmur3(@NonNull String str) {
        return Hashing.murmur3_128().hashString(str, StandardCharsets.UTF_8).toString();
    }

    public static String murmur3_32(@NonNull String str) {
        return Hashing.murmur3_32_fixed().hashString(str, StandardCharsets.UTF_8).toString();
    }

    public static String sha512(@NonNull String str) {
        return Hashing.sha512().hashBytes(str.getBytes(StandardCharsets.UTF_8)).toString();
    }
}
