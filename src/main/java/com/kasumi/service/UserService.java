package com.kasumi.service;

import com.kasumi.core.common.resp.RestResp;
import com.kasumi.dao.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kasumi.dto.req.UserLoginReqDto;
import com.kasumi.dto.req.UserRegisterReqDto;
import com.kasumi.dto.resp.UserLoginRespDto;

import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpRequest;

/**
* @author zhang
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2023-07-13 15:55:16
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userRegisterReqDto
     * @return 新用户 id
     */
    RestResp<Long> userRegister(UserRegisterReqDto userRegisterReqDto);

    /**
     * 用户登录
     * @param userLoginReqDto
     * @return
     */
    RestResp<UserLoginRespDto> userLogin(UserLoginReqDto userLoginReqDto, HttpServletRequest request);

    RestResp<UserLoginRespDto> getLoginRespDto(User user);

    User getLoginUser(HttpServletRequest request);

    /**
     * 是否为管理员
     * @param request
     * @return
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param user
     * @return
     */
    boolean isAdmin(User user);
}
