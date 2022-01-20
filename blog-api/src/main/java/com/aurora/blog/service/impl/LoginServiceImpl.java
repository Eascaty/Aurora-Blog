package com.aurora.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.aurora.blog.dao.pojo.SysUser;
import com.aurora.blog.service.LoginService;
import com.aurora.blog.service.SysUserService;
import com.aurora.blog.utils.JWTUtils;
import com.aurora.blog.vo.ErrorCode;
import com.aurora.blog.vo.Result;
import com.aurora.blog.vo.params.LoginParam;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;
@Service
public class LoginServiceImpl implements LoginService {
    private static final String slat = "mszlu!@#";
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public Result login(LoginParam loginParam) {
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        String pwd = DigestUtils.md5Hex(password + slat);
        SysUser sysUser = sysUserService.findUser(account,pwd);
        if (sysUser == null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(),ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        //登录成功，使用JWT生成token，返回token和redis中
        String token = JWTUtils.createToken(sysUser.getId());
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);
        return Result.success(token);
    }

    @Override
    public SysUser checkToken(String token) {
            if(StringUtils.isBlank(token)) {
                return null;
            }
        Map<String, Object> stringObjectMap = JWTUtils.checkToken(token);
        if(stringObjectMap == null){
            return null;
        }
        String userJson = redisTemplate.opsForValue().get("TOKEN_" + token);
       if(StringUtils.isBlank(userJson)){
           return null;
       }
        SysUser sysUser = JSON.parseObject(userJson, SysUser.class);
        return  sysUser;
    }

    @Override
    public Result logout(String token) {
        redisTemplate.delete("TOKEN_"+token);
        return Result.success(null);
    }

    public static void main(String[] args) {
        System.out.println(DigestUtils.md5Hex("admin"+slat));
    }
}
