package com.aurora.blog.service;

import com.aurora.blog.dao.pojo.SysUser;
import com.aurora.blog.vo.Result;
import org.springframework.stereotype.Service;


public interface SysUserService {

    SysUser findUserById(Long id);

    SysUser findUser(String account, String pwd);

    /**
     * 根据token查询用户信息
     * @param token
     * @return
     */
    Result findUserByToken(String token);

    /**
     * 保存用户
     * @param sysUser
     */
    void save(SysUser sysUser);

    /**
     * 根据账户查找用户
     * @param account
     * @return
     */
    SysUser findUserByAccount(String account);
}
