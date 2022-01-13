package com.aurora.blog.service;

import com.aurora.blog.vo.Result;
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
}
