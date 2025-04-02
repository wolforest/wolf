package com.wolf.common.util.io;

import com.wolf.common.util.lang.StringUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * com.wolf.framework.util.file
 *
 * @author Wingle
 * @since 2022/1/19 上午8:33
 **/
public class DirUtil {
    public static Set<String> listFiles(String dir) {
        return listFiles(dir, 1);
    }

    public static Set<String> listFiles(String dir, int depth) {
        Set<String> result = new HashSet<>();
        if (StringUtil.isBlank(dir)) {
            return result;
        }

        try (Stream<Path> stream = Files.walk(Paths.get(dir), depth)) {
            return stream.filter(file -> !Files.isDirectory(file))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new com.wolf.common.lang.exception.io.IOException(e.getMessage());
        }
    }
}
