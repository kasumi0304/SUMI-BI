package com.kasumi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kasumi.core.common.resp.RestResp;
import com.kasumi.dao.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kasumi.dto.req.UserLoginReqDto;
import com.kasumi.dto.req.UserQueryReqDto;
import com.kasumi.dto.req.UserRegisterReqDto;
import com.kasumi.dto.resp.UserLoginRespDto;
import com.kasumi.dto.resp.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    /**
     * 用户信息脱敏
     * @param user
     * @return
     */
    RestResp<UserLoginRespDto> getLoginRespDto(User user);

    /**
     * 获取已登录用户
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);


    /**
     * 获取用户信息
     * @param user
     * @return
     */
    UserLoginRespDto getLoginUserVO(User user);
}
