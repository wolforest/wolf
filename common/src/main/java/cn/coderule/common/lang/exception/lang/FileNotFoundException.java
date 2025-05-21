package cn.coderule.common.lang.exception.lang;

import cn.coderule.common.lang.exception.SystemException;
import cn.coderule.common.util.lang.string.StringUtil;

public class FileNotFoundException extends SystemException {
    private static final String DEFAULT_MESSAGE = "FileNotFoundException";

    public FileNotFoundException() {
        super(500, DEFAULT_MESSAGE);
    }

    public FileNotFoundException(String className) {
        super(500, StringUtil.join("FileNotFoundException: ", className));
    }
}
