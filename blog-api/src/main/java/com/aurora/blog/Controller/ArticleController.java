package com.aurora.blog.Controller;


import com.aurora.blog.common.aop.LogAnnotation;
import com.aurora.blog.common.cache.Cache;
import com.aurora.blog.service.ArticleService;
import com.aurora.blog.vo.ArticleVo;
import com.aurora.blog.vo.Result;
import com.aurora.blog.vo.params.ArticleParam;
import com.aurora.blog.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


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
//    加上此注解 代表要对此接口记录日志
    @LogAnnotation(module="文章",operator="获取文章列表")
    @Cache(expire = 5 * 60 * 1000, name = "listArticle")
    public Result listArticle(@RequestBody PageParams pageparams){
//        int i =10/0;
        return  articleService.listArticle(pageparams);


    }


    /**
     * 首页 最热文章
     * @return
     */
     @PostMapping("hot")
     @Cache(expire = 5 * 60 * 1000, name = "hot_article")
    public Result hotArticle(){

        int  limit = 5 ;
        return  articleService.hotArticle(limit);

    }


    /**
     * 首页 最新文章
     * @return
     */
    @PostMapping("new")
    @Cache(expire = 5 * 60 * 1000, name = "news_article")
    public Result newArticles(){

        int  limit = 5 ;
        return  articleService.newArticles(limit);

    }

    /**
     * 首页 最新文章
     * @return
     */
    @PostMapping("listArchives")
    public Result listArchives(){

        int  limit = 5 ;
        return  articleService.listArchives();

    }



    @PostMapping("view/{id}")
    public Result findArticleById(@PathVariable("id") Long id) {
       ArticleVo articleVo = articleService.findArticleById(id);

        return Result.success(articleVo);
    }


    @PostMapping("publish")
    public Result publish(@RequestBody ArticleParam articleParam){
        return articleService.publish(articleParam);
    }





}
