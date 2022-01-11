package com.aurora.blog.service;

import com.aurora.blog.vo.params.PageParams;

import javax.xml.transform.Result;

public interface ArticleService {

    /**
     * 分页查询 文章列表
     * @param pageparams
     * @return
     */
     Result listArticle(PageParams pageparams);


}
