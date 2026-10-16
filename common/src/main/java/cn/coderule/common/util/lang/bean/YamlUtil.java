package cn.coderule.common.util.lang.bean;

import java.io.IOException;
import java.io.InputStream;
import org.yaml.snakeyaml.Yaml;

public class YamlUtil {

    public static <T> T load(String fileName, Class<T> clazz) {
        ClassLoader loader = YamlUtil.class.getClassLoader();

        try (InputStream is = loader.getResourceAsStream(fileName)) {
            if (is == null) {
                return null;
            }

            Yaml yaml = new Yaml();
            return yaml.loadAs(is, clazz);
        } catch (IOException ioException) {
            return null;
        }
    }
}
