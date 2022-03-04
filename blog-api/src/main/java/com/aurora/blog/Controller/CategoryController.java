package com.aurora.blog.Controller;

import com.aurora.blog.service.CategoryService;
import com.aurora.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"categorys"})
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    public CategoryController() {
    }

    @GetMapping
    public Result categories() {
        return this.categoryService.findAll();
    }

    @GetMapping({"detail"})
    public Result categoriesDetail() {
        return this.categoryService.findAllDetail();
    }

    @GetMapping({"detail/{id}"})
    public Result categoryDetailById(@PathVariable("id") Long id) {
        return this.categoryService.categoryDetailById(id);
    }
}
