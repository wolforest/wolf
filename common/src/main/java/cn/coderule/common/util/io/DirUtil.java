package cn.coderule.common.util.io;

import cn.coderule.common.util.lang.string.StringUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

/**
 * com.wolf.framework.util.file
 *
 * @author Wingle
 * @since 2022/1/19 上午8:33
 **/
@Slf4j
public class DirUtil {
    public static final String MULTI_PATH_SEPARATOR = ",";

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
            throw new cn.coderule.common.lang.exception.lang.IOException(e.getMessage());
        }
    }

    public static void delete(Path path) {
        delete(path.toString());
    }

    public static void delete(String dir) {
        if (!FileUtil.exists(dir)) {
            return;
        }
        try {
            FileUtils.deleteDirectory(new File(dir));
        } catch (IOException e) {
            throw new cn.coderule.common.lang.exception.lang.IOException(e.getMessage());
        }
    }

    public static void createIfNotExists(String dir) {
        if (StringUtil.isBlank(dir)) {
            return;
        }

        if (!dir.contains(MULTI_PATH_SEPARATOR)) {
            File file = new File(dir);
            if (file.exists()) {
                return;
            }

            boolean result = file.mkdirs();
            if (!result) {
                log.error("create dir fail, dir:{}", dir);
            }
            return;
        }

        String[] dirs = dir.trim().split(MULTI_PATH_SEPARATOR);
        for (String aDir : dirs) {
            createIfNotExists(aDir);
        }
    }

}
