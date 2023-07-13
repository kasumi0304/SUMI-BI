package com.kasumi.core.common.resp;

import com.kasumi.core.common.constant.ErrorCodeEnum;
import lombok.Getter;

import java.io.Serializable;

/**
 * @Author kasumi
 * @Description: Http Rest 相应工具及数据格式封装
 */
@Getter
public class RestResp<T> implements Serializable {

    private int code;

    private T data;

    private String message;

    public RestResp(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    private RestResp() {
        this.code = ErrorCodeEnum.SUCCESS.getCode();
        this.message = ErrorCodeEnum.SUCCESS.getMessage();
    }

    private RestResp(ErrorCodeEnum errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    private RestResp(T data) {
        this();
        this.data = data;
    }


    /**
     *  业务处理成功无需返回数据
     */
    public static RestResp<Void> success(){
        return new RestResp<>();
    }

    /**
     *  业务处理成功，有数据返回
     */
    public static <T> RestResp<T> success(T data){
        return new RestResp<>(data);
    }

    /**
     *  业务处理失败
     */
    public static RestResp<Void> fail(ErrorCodeEnum errorCode){
        return new RestResp<>(errorCode);
    }

    /**
     * 系统错误
     */
    public static RestResp<Void> error() {
        return new RestResp<>(ErrorCodeEnum.SYSTEM_ERROR);
    }
}
