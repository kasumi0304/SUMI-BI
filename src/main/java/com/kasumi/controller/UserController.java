package com.kasumi.controller;

import com.kasumi.core.common.resp.RestResp;
import com.kasumi.dto.req.UserLoginReqDto;
import com.kasumi.dto.req.UserRegisterReqDto;
import com.kasumi.dto.resp.UserLoginRespDto;
import com.kasumi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author kasumi
 * @Description: 用户接口
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     * @param userRegisterReqDto
     * @return
     */
    @PostMapping("/register")
    public RestResp<Long> userRegister(@RequestBody UserRegisterReqDto userRegisterReqDto) {
        return userService.userRegister(userRegisterReqDto);
    }

    /**
     * 用户登录
     *
     * @param userLoginReqDto
     * @param request
     * @return
     */
    @PostMapping("/login")
    public RestResp<UserLoginRespDto> userLogin(@RequestBody UserLoginReqDto userLoginReqDto, HttpServletRequest request) {
        return userService.userLogin(userLoginReqDto, request);
    }

}
