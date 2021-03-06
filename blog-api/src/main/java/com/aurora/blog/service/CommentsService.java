package com.aurora.blog.service;


import com.aurora.blog.vo.Result;
import com.aurora.blog.vo.params.CommentParam;

public interface CommentsService {

    /**
     * 根据文章id 查询所有的评论列表
     * @param id
     * @return
     */
    Result commentsByArticleId(Long id);

    Result comment(CommentParam commentParam);
}
