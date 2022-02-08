package com.aurora.blog.service.impl;

import com.aurora.blog.dao.mapper.SysUserMapper;
import com.aurora.blog.dao.pojo.SysUser;
import com.aurora.blog.service.LoginService;
import com.aurora.blog.service.SysUserService;
import com.aurora.blog.vo.ErrorCode;
import com.aurora.blog.vo.LoginUserVo;
import com.aurora.blog.vo.Result;
import com.aurora.blog.vo.UserVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private LoginService loginService;

    @Override
    public UserVo findUserVoById(Long id) {

        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null) {
            sysUser = new SysUser();
            sysUser.setId(1L);
            sysUser.setAvatar("/static/img/logo.b3a48c0.png");
            sysUser.setNickname("龙哥之路");
        }
        UserVo uservo = new UserVo();
        BeanUtils.copyProperties(sysUser, uservo);
//        uservo.setId(sysUser.getId());
//        uservo.setNickname(uservo.getNickname());
        return uservo;
    }



    @Override
    public SysUser findUserById(Long id) {

        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null) {
            sysUser = new SysUser();
            sysUser.setNickname("龙哥之路");
        }
        return sysUser;
    }

    @Override
    public SysUser findUser(String account, String pwd) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount, account);
        queryWrapper.eq(SysUser::getPassword, pwd);
        queryWrapper.select(SysUser::getId, SysUser::getAccount, SysUser::getAvatar, SysUser::getNickname);
        queryWrapper.last("limit 1");
        SysUser sysUser = sysUserMapper.selectOne(queryWrapper);
        return sysUser;
    }

    @Override
    public Result findUserByToken(String token) {
        /**
         * 1.token  合法性校验
         *     是否为空，解析是否成功  redis是否存在
         * 2.如果校验失败 返回错误
         * 3.如果成功，返回对应结果 LoginUserVo
         *
         */
        SysUser sysUser =loginService.checkToken(token);
        if (sysUser == null) {

             Result.fail(ErrorCode.TOKEN_ERROR.getCode(), ErrorCode.TOKEN_ERROR.getMsg());
        }
        LoginUserVo loginUserVo = new LoginUserVo();
        loginUserVo.setId(sysUser.getId());
        loginUserVo.setNickname(sysUser.getNickname());
        loginUserVo.setAvatar(sysUser.getAvatar());
        loginUserVo.setAccount(sysUser.getAccount());
        return Result.success(loginUserVo);
    }

    @Override
    public void save(SysUser sysUser) {
//           保存用户，id会自动生成
//          这个地方 默认生成的id是 分布式id 雪花算法
//        mybatis-plus
        this.sysUserMapper.insert(sysUser);

    }

    @Override
    public SysUser findUserByAccount(String account) {
            LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysUser::getAccount,account);
            queryWrapper.last("limit 1");
            return this.sysUserMapper.selectOne(queryWrapper);

    }

}
