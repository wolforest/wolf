package cn.coderule.common.util.lang.bean;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import lombok.Data;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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

        assertNotNull("YamlUtil.loadResource should not return null", sample);
        assertEquals("YamlUtil.loadResource failed to map name", "wolf", sample.getName());
        assertEquals("YamlUtil.loadResource failed to map version", "1.0.0", sample.getVersion());
        assertTrue("YamlUtil.loadResource failed to map enabled", sample.isEnabled());
        assertEquals("YamlUtil.loadResource failed to map tags", List.of("java", "yaml"), sample.getTags());
        assertNotNull("YamlUtil.loadResource failed to map server", sample.getServer());
        assertEquals("YamlUtil.loadResource failed to map server.host", "127.0.0.1", sample.getServer().getHost());
        assertEquals("YamlUtil.loadResource failed to map server.port", 8080, sample.getServer().getPort());
    }

    @Test(expected = FileNotFoundException.class)
    public void loadResource_should_throw_when_resource_not_found() throws IOException {
        YamlUtil.loadResource("not_exists.yaml", YamlSample.class);
    }

    @Test
    public void load_should_map_file_yaml_to_object() throws IOException {
        Path file = Files.createTempFile("yaml_util_test", ".yaml");
        try {
            Files.writeString(file, YAML_CONTENT);

            YamlSample sample = YamlUtil.load(file.toString(), YamlSample.class);

            assertNotNull("YamlUtil.load should not return null", sample);
            assertEquals("YamlUtil.load failed to map name", "wolf", sample.getName());
            assertEquals("YamlUtil.load failed to map server.port", 8080, sample.getServer().getPort());
        } finally {
            Files.deleteIfExists(file);
        }
    }

    @Test(expected = IOException.class)
    public void load_should_throw_when_file_not_found() throws IOException {
        YamlUtil.load("/not/exists/yaml_util_test.yaml", YamlSample.class);
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
