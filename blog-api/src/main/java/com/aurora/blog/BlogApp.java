package com.aurora.blog;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Repository;

@MapperScan(annotationClass = Repository.class, basePackages = "com.aurora.blog")
@SpringBootApplication
public class BlogApp {

        public static void main(String[] args){
            SpringApplication.run(BlogApp.class,args);
        }
}
