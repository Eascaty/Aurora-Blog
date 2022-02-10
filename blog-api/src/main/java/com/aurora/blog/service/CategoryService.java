package com.aurora.blog.service;

import com.aurora.blog.vo.CategoryVo;
import com.aurora.blog.vo.Result;


public interface CategoryService {

    CategoryVo findCategoryById(Long id);

    Result findAll();
}