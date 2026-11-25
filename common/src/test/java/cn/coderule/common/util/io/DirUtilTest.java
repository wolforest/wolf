package cn.coderule.common.util.io;

import cn.coderule.common.util.lang.string.StringUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DirUtilTest {

    @Test
    public void createIfNotExists() {
        String tmpDir = System.getProperty("java.io.tmpdir");
        String path = tmpDir + StringUtil.uuid();

        DirUtil.createIfNotExists(path);
        assertTrue(FileUtil.exists(path), "createIfNotExists failed: ");

        FileUtil.delete(path);

    }
}
