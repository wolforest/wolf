package cn.coderule.common.util.lang.bean;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import lombok.Data;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * for junit test
 */
public class YamlUtilTest {

    private static final String YAML_CONTENT = "name: wolf\n"
            + "version: 1.0.0\n"
            + "enabled: true\n"
            + "tags:\n"
            + "  - java\n"
            + "  - yaml\n"
            + "server:\n"
            + "  host: 127.0.0.1\n"
            + "  port: 8080\n";

    @Test
    public void loadResource_should_map_resource_yaml_to_object() throws IOException {
        YamlSample sample = YamlUtil.loadResource("yaml_util_test.yaml", YamlSample.class);

        assertNotNull(sample, "YamlUtil.loadResource should not return null");
        assertEquals("wolf", sample.getName(), "YamlUtil.loadResource failed to map name");
        assertEquals("1.0.0", sample.getVersion(), "YamlUtil.loadResource failed to map version");
        assertTrue(sample.isEnabled(), "YamlUtil.loadResource failed to map enabled");
        assertEquals(List.of("java", "yaml"), sample.getTags(), "YamlUtil.loadResource failed to map tags");
        assertNotNull(sample.getServer(), "YamlUtil.loadResource failed to map server");
        assertEquals("127.0.0.1", sample.getServer().getHost(), "YamlUtil.loadResource failed to map server.host");
        assertEquals(8080, sample.getServer().getPort(), "YamlUtil.loadResource failed to map server.port");
    }

    @Test
    public void loadResource_should_throw_when_resource_not_found() {
        assertThrows(FileNotFoundException.class,
                () -> YamlUtil.loadResource("not_exists.yaml", YamlSample.class));
    }

    @Test
    public void load_should_map_file_yaml_to_object() throws IOException {
        Path file = Files.createTempFile("yaml_util_test", ".yaml");
        try {
            Files.writeString(file, YAML_CONTENT);

            YamlSample sample = YamlUtil.load(file.toString(), YamlSample.class);

            assertNotNull(sample, "YamlUtil.load should not return null");
            assertEquals("wolf", sample.getName(), "YamlUtil.load failed to map name");
            assertEquals(8080, sample.getServer().getPort(), "YamlUtil.load failed to map server.port");
        } finally {
            Files.deleteIfExists(file);
        }
    }

    @Test
    public void load_should_throw_when_file_not_found() {
        assertThrows(IOException.class,
                () -> YamlUtil.load("/not/exists/yaml_util_test.yaml", YamlSample.class));
    }

    @Data
    public static class YamlSample {
        private String name;
        private String version;
        private boolean enabled;
        private List<String> tags;
        private Server server;
    }

    @Data
    public static class Server {
        private String host;
        private int port;
    }
}
