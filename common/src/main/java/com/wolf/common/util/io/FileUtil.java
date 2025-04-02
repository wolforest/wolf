package com.wolf.common.util.io;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;
import com.wolf.common.lang.exception.lang.FileNotFoundException;
import com.wolf.common.lang.exception.method.MethodExecuteFailException;
import com.wolf.common.util.lang.StringUtil;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * com.wolf.common.io.file
 *
 * @author Wingle
 * @since 2021/1/24 1:10 上午
 **/
@Slf4j
public class FileUtil {

    public static InputStream getResourceInputStream(String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
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

    public static boolean isFilePath(String path) {
        if (StringUtil.isBlank(path)) {
            return false;
        }

        String pattern = "^.*\\.[a-zA-Z0-9]+$";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(path);
        return matcher.find();

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

    public static JSONObject loadJson(String path) {
        InputStream inputStream = getResourceStream(path);
        try {
            return JSON.parseObject(inputStream, JSONObject.class, JSONReader.Feature.ErrorOnEnumNotMatch);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new MethodExecuteFailException("read file failed : " + path);
        }
    }

    public static String loadString(String path) {
        InputStream inputStream = getResourceStream(path);
        StringBuilder sb = new StringBuilder();

        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new MethodExecuteFailException("read file failed : " + path);
        }
    }

    public static InputStream getResourceStream(String path) {
        if (StringUtil.isBlank(path)) {
            throw new IllegalArgumentException("path can't be blank");
        }

        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        if (is == null) {
            throw new IllegalArgumentException("path not found");
        }
        return is;
    }

    public static String loadString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static InputStream getFileStream(String path) {
        File file = new File(path);

        try {
            return new FileInputStream(file);
        } catch (Exception e) {
            throw new FileNotFoundException(path);
        }
    }

    public static byte[] getResourceBytes(String path) {
        InputStream inputStream = getResourceStream(path);
        try {
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new MethodExecuteFailException("fail to convert inputSteam to byte array : " + path);
        }
    }

    public static boolean isFileExists(String path) {
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

    public static List<String> listFolderFiles(String dirPath, boolean includeSubDir, boolean sortByLastModified) {
        List<String> files = new ArrayList<>();
        File file = new File(dirPath);
        File[] fs = file.listFiles();
        if (null == fs || fs.length == 0) {
            return files;
        }

        if (sortByLastModified) {
            Arrays.sort(fs, Comparator.comparingLong(File::lastModified).reversed());
        }

        for (File f : fs) {
            if (f.isDirectory() && includeSubDir) {
                files.addAll(listFolderFiles(f.getAbsolutePath(), true, sortByLastModified));
            } else if (f.isFile()) {
                files.add(f.getAbsolutePath());
            }
        }

        return files;
    }
}
