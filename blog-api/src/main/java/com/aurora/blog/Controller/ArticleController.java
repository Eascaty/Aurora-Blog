package com.aurora.blog.Controller;


import com.aurora.blog.service.ArticleService;
import com.aurora.blog.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.transform.Result;

//json数据进行交互
@RestController
@RequestMapping("articles")
public class ArticleController {

    @Autowired
    ArticleService articleService;

    /**
     *  首页 文章列表
     * @param pageparams
     * @return
     */
    @PostMapping
    public Result listArticle(@RequestBody PageParams pageparams){


        return  articleService.listArticle(pageparams);
    }
}
