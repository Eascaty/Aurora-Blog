package com.aurora.blog.service;

import com.aurora.blog.dao.pojo.SysUser;
import com.aurora.blog.vo.params.LoginParam;
import com.aurora.blog.vo.Result;
import org.springframework.stereotype.Service;


public interface LoginService {
    /**
     * 登录
     * @param loginParam
     * @return
     */
    Result login(LoginParam loginParam);

    SysUser checkToken(String token);
}
