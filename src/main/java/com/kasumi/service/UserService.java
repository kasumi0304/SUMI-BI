package com.kasumi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kasumi.core.common.resp.RestResp;
import com.kasumi.dao.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kasumi.dto.req.UserLoginReqDto;
import com.kasumi.dto.req.UserQueryRequest;
import com.kasumi.dto.req.UserRegisterReqDto;
import com.kasumi.dto.resp.UserLoginRespDto;
import com.kasumi.dto.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpRequest;
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

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 获取脱敏的已登录用户信息
     *
     * @return
     */
    UserLoginRespDto getLoginUserVO(User user);

    /**
     * 获取脱敏的用户信息
     *
     * @param user
     * @return
     */
    UserVO getUserVO(User user);

    /**
     * 获取脱敏的用户信息
     *
     * @param userList
     * @return
     */
    List<UserVO> getUserVO(List<User> userList);

    /**
     * 获取查询条件
     *
     * @param userQueryRequest
     * @return
     */
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);
}
