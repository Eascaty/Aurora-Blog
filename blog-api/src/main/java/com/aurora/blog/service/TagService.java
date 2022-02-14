package com.aurora.blog.service;

import com.aurora.blog.vo.Result;
import com.aurora.blog.vo.TagVo;
import org.springframework.stereotype.Service;

import java.util.List;


public interface TagService {
     List<TagVo> findTagsByArticleId(Long articleId);

    Result hots(int limit);

    Result findAll();

    Result findAllDetail();

    Result findDetailById(Long id);
}
