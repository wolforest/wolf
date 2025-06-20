package cn.coderule.common.lang.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import lombok.Setter;

/**
 * Non-thread-safe
 */
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class BaseException extends RuntimeException {
    protected long code;
    protected Map<String, String> data = new HashMap<>();

    public BaseException(String message) {
        this(100, message);
    }

    public BaseException(String message, Throwable t) {
        this(100, message, t);
    }

    public BaseException(long code, String message) {
        this(code, message, null);
    }

    public BaseException(long code, String message, Throwable t) {
        super(message, t);
        this.code = code;
    }

    public void setDataRow(String key, String value) {
        this.data.put(key, value);
    }

    public void addData(Map<String, String> map) {
        this.data.putAll(map);
    }
}
