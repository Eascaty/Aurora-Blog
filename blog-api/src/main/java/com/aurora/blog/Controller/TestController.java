package com.aurora.blog.Controller;

import com.aurora.blog.dao.pojo.SysUser;
import com.aurora.blog.service.TagService;
import com.aurora.blog.utils.UserThreadLocal;
import com.aurora.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {

    @RequestMapping
    public Result test(){
        SysUser sysuser = UserThreadLocal.get();
        System.out.println(sysuser);
        return Result.success(null);
    }
}
