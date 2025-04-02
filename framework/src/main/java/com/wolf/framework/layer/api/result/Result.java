package com.wolf.framework.layer.api.result;

import lombok.Data;
import com.wolf.common.lang.exception.api.NullReturnedException;

import java.io.Serializable;
import lombok.experimental.SuperBuilder;

/**
 * com.wolf.framework.rpc
 *
 * @author Wingle
 * @since 2019/11/6 4:18 下午
 **/
@Data
@SuperBuilder
public class Result<T> implements Serializable {
    public static final String DEFAULT_SUCCESS_CODE = "SUCCESS";
    public static final String DEFAULT_SUCCESS_MESSAGE = "";

    public static final String DEFAULT_FAILURE_CODE = "FAILURE";
    public static final String DEFAULT_MESSAGE = "";
    public static final String DEFAULT_FAILURE_MESSAGE = "";
    public static final int SUCCESS_CODE_TYPE = 0;


    protected boolean success;
    /**
     * code: explicit result sign
     *  - maybe the business exception
     *      - NoSuchProduct
     *      - OrderNotFound
     *      - ...
     *  - maybe just mark success or failure
     *      - SUCCESS
     *      - FAILURE
     */
    protected String code;

    /**
     * errorType:
     *  - 0: success response
     *  - 10: system error
     *  - 20: business error
     *  - 30: needLogin error
     *  - 40: noPermission error
     *  - 50: illegalArgument error
     */
    protected int codeType = CodeTypeEnum.SUCCESS.getCode();

    /**
     * message
     */
    protected String message;

    /**
     * the data
     */
    protected T data;

    public static Result<Object> success(){
        return success(null);
    }

    public static <T> Result<T> success(String code, String message, T t){
        return new Result<>(true, code, message, t);
    }

    public static <T> Result<T> success(String message, T t){
        return new Result<>(true, DEFAULT_SUCCESS_CODE, message, t);
    }

    public static <T> Result<T> success(T t){
        return new Result<>(true, DEFAULT_SUCCESS_CODE, DEFAULT_SUCCESS_MESSAGE, t);
    }

    public static <T> Result<T> failure(String message) {
        return failure(DEFAULT_FAILURE_CODE, message);
    }

    public static <T> Result<T> failure(String code, String message) {
        return failure(code, message, null);
    }

    public static <T> Result<T> failure(String code, String message, T t) {
        Result<T> result = new Result<>(false, code, message, t);
        result.setCodeType(CodeTypeEnum.BUSINESS_ERROR.getCode());
        return result;
    }

    public static <T> Result<T> create(boolean success, String code, String message) {
        return create(success, code, message, null);
    }

    public static <T> Result<T> create(boolean success, String code, String message, T t) {
        return new Result<>(success, code, message, t);
    }

    public Result() {}

    public Result(boolean success) {
        this(success, DEFAULT_SUCCESS_CODE, DEFAULT_MESSAGE, null);
    }

    public Result(boolean success, T data) {
        this(success, DEFAULT_SUCCESS_CODE, DEFAULT_MESSAGE, data);
    }

    public Result(boolean success, String code, String message) {
        this(success, code, message, null);
    }

    public Result(boolean success, String code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public T notNullData() {
        return notNullData(null);
    }

    public T notNullData(String msg) {
        if (!success || null == data) {
            if (msg != null) {
                throw new NullReturnedException(msg);
            }
            throw new NullReturnedException();
        }

        return data;
    }
}
