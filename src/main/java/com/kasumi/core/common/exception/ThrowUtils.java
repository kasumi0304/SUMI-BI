package com.kasumi.core.common.exception;


import com.kasumi.core.common.constant.ErrorCodeEnum;

/**
 * 抛异常工具类
 */
public class ThrowUtils {

    /**
     * 条件成立则抛异常
     *
     * @param condition
     * @param runtimeException
     */
    public static void throwIf(boolean condition, RuntimeException runtimeException) {
        if (condition) {
            throw runtimeException;
        }
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition
     * @param errorCode
     */
    public static void throwIf(boolean condition, ErrorCodeEnum errorCode, String message) {
        throwIf(condition, new BusinessException(errorCode, message));
    }

    public static void throwIf(boolean condition, ErrorCodeEnum errorCode) {
        throwIf(condition, new BusinessException(errorCode));
    }

}
