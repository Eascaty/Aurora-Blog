package com.aurora.blog.Controller;

import com.aurora.blog.service.TagService;
import com.aurora.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tags")
public class TagController {

    @Autowired
    private TagService tagService;
//         /tags/hot
      @GetMapping("hot")
        public Result hot(){
            int  limit = 6;
           return  tagService.hots(limit);
        }

    @GetMapping
    public Result findAll(){

        return tagService.findAll();
    }

}
