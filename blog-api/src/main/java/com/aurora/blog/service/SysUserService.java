package com.aurora.blog.service;

import com.aurora.blog.dao.pojo.SysUser;
import org.springframework.stereotype.Service;


public interface SysUserService {

    SysUser findUserById(Long id);

    SysUser findUser(String account, String pwd);
}
