package cn.coderule.common.util.io;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import cn.coderule.common.lang.exception.SystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;
import cn.coderule.common.lang.exception.lang.FileNotFoundException;
import cn.coderule.common.util.lang.StringUtil;

import java.io.*;
import java.net.URL;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * com.wolf.common.io.file
 *
 * @author Wingle
 * @since 2021/1/24 1:10 上午
 **/
@Slf4j
public class FileUtil {
    private static final String BAK_SUFFIX = ".bak";

    public static String fileToString(String path) {
        boolean pathExists = exists(path);
        boolean bakExists = exists(path + BAK_SUFFIX);

        if (!pathExists && !bakExists) {
            return null;
        }

        String realPath = pathExists ? path : path + BAK_SUFFIX;
        return readString(realPath);
    }

    /**
     * It's not thread safe
     * @param content content
     * @param path filePath
     */
    public static void stringToFile(String content, String path) {
        if (exists(path + BAK_SUFFIX)) {
            delete(path + BAK_SUFFIX);
        }

        if (exists(path)) {
            rename(path, path + BAK_SUFFIX);
        }

        writeString(content, path);
    }

    public static void writeString(String content, File file) {
        try (OutputStream os = Files.newOutputStream(Path.of(file.getAbsolutePath()))) {
            os.write(content.getBytes(UTF_8));
        } catch (IOException e) {
            throw new cn.coderule.common.lang.exception.lang.IOException(e.getMessage());
        }
    }

    public static void writeString(String content, String path) {
        try (OutputStream os = Files.newOutputStream(Path.of(path))) {
            os.write(content.getBytes(UTF_8));
        } catch (IOException e) {
            throw new cn.coderule.common.lang.exception.lang.IOException(e.getMessage());
        }
    }

    public static String getExtension(String filename) {
        return getExtension(filename, false);
    }

    public static String getExtension(String filename, boolean withDot) {
        int idx = withDot ? 0 : 1;
        Optional<String> result = Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + idx));

        return result.orElse("");
    }

    public static JSONObject readJSON(String path) {
        try {
            InputStream inputStream = Files.newInputStream(Path.of(path));
            return JSON.parseObject(inputStream, JSONObject.class, JSONReader.Feature.ErrorOnEnumNotMatch);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new SystemException("read file failed : " + path);
        }
    }

    public static String readString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static String readString(String path) {
        try (Reader reader = new InputStreamReader(Files.newInputStream(Path.of(path)))) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new cn.coderule.common.lang.exception.lang.IOException(e.getMessage());
        }
    }

    public static byte[] readBytes(String path) {
        try {
            InputStream inputStream = Files.newInputStream(Path.of(path));
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new SystemException("fail to convert inputSteam to byte array : " + path);
        }
    }

    public static boolean exists(String path) {
        if (StringUtil.isBlank(path)) {
            return false;
        }

        File file = new File(path);
        return file.exists();
    }

    public static String getFileName(String path) {
        if (StringUtil.isBlank(path)) {
            return "";
        }

        File file = new File(path);
        return file.getName();
    }

    public static void rename(String from, String to) {
        File fromFile = new File(from);
        File toFile = new File(to);

        if (!fromFile.exists() || toFile.exists()) {
            throw new cn.coderule.common.lang.exception.lang.IOException("Can't rename file: " + from + " to " + to);
        }

        boolean result = fromFile.renameTo(toFile);
        if (!result) {
            throw new cn.coderule.common.lang.exception.lang.IOException("Can't rename file: " + from + " to " + to);
        }
    }

    public static void delete(String path) {
        File file = new File(path);
        delete(file);
    }

    public static void delete(File file) {
        if (!file.exists()) {
            return;
        }

        boolean result = file.delete();
        if (!result) {
            throw new cn.coderule.common.lang.exception.lang.IOException("Can't delete file: " + file.getName());
        }
    }

    public static boolean deleteQuietly(String path) {
        File file = new File(path);
        return FileUtils.deleteQuietly(file);
    }

    public static boolean deleteQuietly(File file) {
        try {
            return file.delete();
        } catch (Exception var2) {
            return false;
        }
    }

    public static FileReader getResourceFileReader(String path) {
        URL url = Thread.currentThread().getContextClassLoader().getResource(path);
        if (url == null) {
            throw new FileNotFoundException(path);
        }

        try {
            return new FileReader(url.getFile());
        } catch (java.io.FileNotFoundException e) {
            throw new FileNotFoundException(path);
        }
    }

}
