package com.kasumi.dto.req;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author kasumi
 * @Description: 用户登录 请求DTO
 */
@Data
public class UserLoginReqDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String userAccount;

    private String userPassword;
}
