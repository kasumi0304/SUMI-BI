package com.kasumi.core.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author kasumi
 * @Description: 错误码枚举类
 */

@Getter
@AllArgsConstructor
public enum ErrorCodeEnum {

    /**
     * 正确执行后的返回
     */
    SUCCESS(0, "一切 ok"),

    /**
     * 一级宏观错误码，用户端错误
     */
    USER_ERROR(40010, "用户端错误"),

    /**
     * 二级宏观错误码，用户注册错误
     */
    USER_REGISTER_ERROR(40100, "用户注册错误"),

    /**
     * 用户未同意隐私协议
     */
    USER_NO_AGREE_PRIVATE_ERROR(40101, "用户未同意隐私协议"),

    /**
     * 注册国家或地区受限
     */
    USER_REGISTER_AREA_LIMIT_ERROR(40102, "注册国家或地区受限"),

    /**
     * 用户验证码错误
     */
    USER_VERIFY_CODE_ERROR(40240, "用户验证码错误"),

    /**
     * 用户名已存在
     */
    USER_NAME_EXIST(40111, "用户名已存在"),

    /**
     * 用户账号不存在
     */
    USER_ACCOUNT_NOT_EXIST(40201, "用户账号不存在"),

    /**
     * 用户密码错误
     */
    USER_PASSWORD_ERROR(40210, "用户密码错误"),

    /**
     * 二级宏观错误码，用户请求参数错误
     */
    USER_REQUEST_PARAM_ERROR(40400, "用户请求参数错误"),

    /**
     * 用户登录已过期
     */
    USER_LOGIN_EXPIRED(40230, "用户登录已过期"),

    /**
     * 访问未授权
     */
    USER_UN_AUTH(40301, "访问未授权"),

    /**
     * 用户请求服务异常
     */

    USER_REQ_EXCEPTION(40500, "用户请求服务异常"),

    /**
     * 请求超出限制
     */
    USER_REQ_MANY(405014, "请求超出限制"),

    /**
     * 用户上传文件异常
     */
    USER_UPLOAD_FILE_ERROR(40700, "用户上传文件异常"),

    /**
     * 用户上传文件类型不匹配
     */
    USER_UPLOAD_FILE_TYPE_NOT_MATCH(40701, "用户上传文件类型不匹配"),

    /**
     * 一级宏观错误码，系统执行出错
     */
    SYSTEM_ERROR(50001, "系统执行出错"),

    /**
     * 二级宏观错误码，系统执行超时
     */
    SYSTEM_TIMEOUT_ERROR(50100, "系统执行超时"),

    /**
     * 一级宏观错误码，调用第三方服务出错
     */
    THIRD_SERVICE_ERROR(60001, "调用第三方服务出错"),

    /**
     * 一级宏观错误码，中间件服务出错
     */
    MIDDLEWARE_SERVICE_ERROR(60100, "中间件服务出错"),


    TOO_MANY_REQUEST(42900, "过多请求"),

    NOT_FOUND_ERROR(40400, "请求数据不存在");

    /**
     * 状态码
     */
    private final int code;

    /**
     * 信息
     */
    private final String message;
}
