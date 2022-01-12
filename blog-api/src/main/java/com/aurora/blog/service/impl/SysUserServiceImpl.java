package com.aurora.blog.service.impl;

import com.aurora.blog.dao.mapper.SysUserMapper;
import com.aurora.blog.dao.pojo.SysUser;
import com.aurora.blog.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public SysUser findUserById(Long id) {

        SysUser sysUser = sysUserMapper.selectById(id);
        if(sysUser == null){
            sysUser = new SysUser();
            sysUser.setNickname("龙哥之路");
        }
        return sysUser;
    }
}
