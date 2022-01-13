package com.aurora.blog.handler;

import com.aurora.blog.vo.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//对加了@Controller注解的方法进行拦截处理 AOP
@ControllerAdvice
public class AllExceptionHandler {
//        进行异常处理，处理Exception.class的异常
        @ExceptionHandler(Exception.class)
        @ResponseBody
        public Result doException(Exception exception){
            exception.printStackTrace();
            return Result.fail(-999,"系统异常");
        }



}
