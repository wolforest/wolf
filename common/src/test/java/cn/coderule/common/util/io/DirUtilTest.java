package cn.coderule.common.util.io;

import cn.coderule.common.util.lang.StringUtil;
import java.io.File;
import org.junit.Test;

import static org.junit.Assert.*;

public class DirUtilTest {

    @Test
    public void createIfNotExists() {
        String tmpDir = System.getProperty("java.io.tmpdir");
        String path = tmpDir + StringUtil.uuid();

        DirUtil.createIfNotExists(path);
        assertTrue("createIfNotExists failed: ", FileUtil.exists(path));

        FileUtil.delete(path);

    }
}
