package com.wolf.common.util;

import com.wolf.common.util.io.FileUtil;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Slf4j
public class FileUtilTest extends TestCase {


    public void testGetFileName() {
        String path = "/User/nino/code/test.txt";
        assertEquals("File.getFileName Failed", FileUtil.getFileName(path), "test.txt");
    }

    public void testGetFileNameWithoutExtension() {
        String path = "/User/nino/code/test";
        assertEquals("File.getFileName Failed", FileUtil.getFileName(path), "test");
    }

    public void test_resource_reader_work() {
        FileReader fileReader = FileUtil.getResourceFileReader("resource_read_test.txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        try {
            String line = bufferedReader.readLine();
            assertEquals("FileUtil.getResourceFileReader fail", "wolf", line);
        } catch (IOException e) {
            log.error("resource_read_test.txt doesn't exists");
        }
    }
}
