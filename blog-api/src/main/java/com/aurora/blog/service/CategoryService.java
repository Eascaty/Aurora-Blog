package com.aurora.blog.service;

import com.aurora.blog.vo.CategoryVo;



public interface CategoryService {

    CategoryVo findCategoryById(Long id);
}