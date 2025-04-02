package cn.coderule.common.lang.exception.lang;

import lombok.Getter;
import cn.coderule.common.lang.exception.SystemException;

@Getter
public class IOException extends SystemException {
    private static final String DEFAULT_MESSAGE = "IOException";
    public IOException() {
        super(100601, DEFAULT_MESSAGE);
    }

    public IOException(String message) {
        super(100601, message);
    }

    public IOException(long code, String message) {
        super(code, message);
    }
}
