package cn.coderule.common.util;

import org.junit.jupiter.api.Test;
import cn.coderule.common.util.io.FileUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class FileUtilTest {


    @Test


    public void testGetFileName() {
        String path = "/User/xxx/code/test.txt";
        assertEquals(FileUtil.getFileName(path), "test.txt", "File.getFileName Failed");
    }

    @Test

    public void testGetFileNameWithoutExtension() {
        String path = "/User/xxx/code/test";
        assertEquals(FileUtil.getFileName(path), "test", "File.getFileName Failed");
    }

    @Test

    public void test_resource_reader_work() {
        FileReader fileReader = FileUtil.getResourceFileReader("resource_read_test.txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        try {
            String line = bufferedReader.readLine();
            assertEquals("wolf", line, "FileUtil.getResourceFileReader fail");
        } catch (IOException e) {
            log.error("resource_read_test.txt doesn't exists");
        }
    }
}
