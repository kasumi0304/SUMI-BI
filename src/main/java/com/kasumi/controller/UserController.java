package com.kasumi.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kasumi.core.common.constant.ErrorCodeEnum;
import com.kasumi.core.common.exception.BusinessException;
import com.kasumi.core.common.exception.ThrowUtils;
import com.kasumi.core.common.resp.RestResp;
import com.kasumi.dao.entity.User;
import com.kasumi.dto.req.DeleteReqDto;
import com.kasumi.dto.req.UserLoginReqDto;
import com.kasumi.dto.req.UserQueryReqDto;
import com.kasumi.dto.req.UserRegisterReqDto;
import com.kasumi.dto.resp.UserLoginRespDto;
import com.kasumi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
     * 用户注册接口
     * @param userRegisterReqDto
     * @return
     */
    @PostMapping("/register")
    public RestResp<Long> userRegister(@RequestBody UserRegisterReqDto userRegisterReqDto) {
        return userService.userRegister(userRegisterReqDto);
    }

    /**
     * 用户登录接口
     *
     * @param userLoginReqDto
     * @param request
     * @return
     */
    @PostMapping("/login")
    public RestResp<UserLoginRespDto> userLogin(@RequestBody UserLoginReqDto userLoginReqDto, HttpServletRequest request) {
        return userService.userLogin(userLoginReqDto, request);
    }


    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @GetMapping("/get/login")
    public RestResp<UserLoginRespDto> getLoginUser(HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        return RestResp.success(userService.getLoginUserVO(user));
    }




}
