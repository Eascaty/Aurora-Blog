package com.aurora.blog.service;

import com.aurora.blog.vo.ArticleVo;
import com.aurora.blog.vo.Result;
import com.aurora.blog.vo.params.ArticleParam;
import com.aurora.blog.vo.params.PageParams;

public interface ArticleService {

    /**
     * 分页查询 文章列表
     * @param pageparams
     * @return
     */
     Result listArticle(PageParams pageparams);

    /**
     * 最热文章
     * @param limit
     * @return
     */
     Result hotArticle(int limit);

    /**
     * 最新文章
     * @param limit
     * @return
     */
    Result newArticles(int limit);

    /**
     * 文章归档
     * @return
     */
    Result listArchives();

    /**
     * 查看文章详情
     * @param
     * @return
     */
   ArticleVo findArticleById(Long id);


    Result publish(ArticleParam articleParam);
}
