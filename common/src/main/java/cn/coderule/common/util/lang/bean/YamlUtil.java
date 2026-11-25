package cn.coderule.common.util.lang.bean;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.yaml.snakeyaml.Yaml;

public class YamlUtil {

    public static <T> T loadResource(String fileName, Class<T> clazz) throws IOException {
        ClassLoader classLoader = clazz.getClassLoader();

        try (InputStream inputStream = classLoader.getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new FileNotFoundException(fileName);
            }

            Yaml yaml = new Yaml();
            return yaml.loadAs(inputStream, clazz);
        }
    }

    public static <T> T load(String fileName, Class<T> clazz) throws IOException {
        try (InputStream is = Files.newInputStream(Path.of(fileName))) {
            Yaml yaml = new Yaml();
            return yaml.loadAs(is, clazz);
        }
    }
}
