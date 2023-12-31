package com.kasumi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kasumi.core.common.constant.ErrorCodeEnum;
import com.kasumi.core.common.exception.BusinessException;
import com.kasumi.core.common.resp.RestResp;
import com.kasumi.dao.entity.User;
import com.kasumi.dto.req.UserLoginReqDto;
import com.kasumi.dto.req.UserRegisterReqDto;
import com.kasumi.dto.resp.UserLoginRespDto;
import com.kasumi.service.UserService;
import com.kasumi.dao.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;

import static com.kasumi.core.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author zhang
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2023-07-13 15:55:16
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "kasumi";

    private static final Object LOCK = new Object(); // 对象锁

    @Override
    public RestResp<Long> userRegister(UserRegisterReqDto userRegisterReqDto) {
        String userAccount = userRegisterReqDto.getUserAccount();
        String userPassword = userRegisterReqDto.getUserPassword();
        String checkPassword = userRegisterReqDto.getCheckPassword();
        //  校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCodeEnum.USER_REQUEST_PARAM_ERROR);
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCodeEnum.USER_REQUEST_PARAM_ERROR);
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCodeEnum.USER_REQUEST_PARAM_ERROR);
        }
        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCodeEnum.USER_REQUEST_PARAM_ERROR);
        }

        synchronized (LOCK) {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("userAccount", userAccount);
            //  校验手机号是否注册
            if (this.baseMapper.selectCount(queryWrapper) > 0) {
                throw new BusinessException(ErrorCodeEnum.USER_NAME_EXIST);
            }

            //  加密
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
            //  插入数据
            User user = new User();
            //默认设置用户昵称为用户名
            user.setUserName(userAccount);
            //设置默认头像
            user.setUserAvatar("https://kasumi-1314099652.cos.ap-guangzhou.myqcloud.com/IMG_2270.JPG");
            user.setUserAccount(userAccount);
            user.setUserPassword(encryptPassword);
            //  数据库保存失败
            if (!this.save(user)) {
                throw new BusinessException(ErrorCodeEnum.SYSTEM_ERROR);
            }
            return RestResp.success(user.getId());
        }
    }

    @Override
    public RestResp<UserLoginRespDto> userLogin(UserLoginReqDto userLoginReqDto, HttpServletRequest request) {
        // 1. 校验
        String userAccount = userLoginReqDto.getUserAccount();
        String userPassword = userLoginReqDto.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCodeEnum.USER_REQUEST_PARAM_ERROR);
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCodeEnum.USER_REQUEST_PARAM_ERROR);
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCodeEnum.USER_REQUEST_PARAM_ERROR);
        }
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = this.baseMapper.selectOne(queryWrapper);
        // 用户不存在
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCodeEnum.USER_ACCOUNT_NOT_EXIST);
        }
        // 3. 记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, user);
        return this.getLoginRespDto(user);
    }

    @Override
    public RestResp<UserLoginRespDto> getLoginRespDto(User user) {
        UserLoginRespDto userLoginRespDto = new UserLoginRespDto();
        BeanUtils.copyProperties(user, userLoginRespDto);
        return RestResp.success(userLoginRespDto);
    }

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCodeEnum.USER_LOGIN_EXPIRED);
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        currentUser = this.getById(userId);
        if (currentUser == null) {
            throw new BusinessException(ErrorCodeEnum.USER_LOGIN_EXPIRED);
        }
        return currentUser;
    }




    @Override
    public UserLoginRespDto getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserLoginRespDto loginUserVO = new UserLoginRespDto();
        BeanUtils.copyProperties(user, loginUserVO);
        return loginUserVO;
    }


}




