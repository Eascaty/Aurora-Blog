package com.aurora.blog.Controller;

import com.aurora.blog.service.CommentsService;
import com.aurora.blog.service.TagService;
import com.aurora.blog.vo.Result;
import com.aurora.blog.vo.params.CommentParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("comments")
public class CommentsController {

//    /comments/article/{id}
    @Autowired
   private  CommentsService commentsService;
    @Autowired
    private TagService tagService;

    @GetMapping("article/{id}")
    public Result comments(@PathVariable("id")Long id){

        return commentsService.commentsByArticleId(id);
    }
    @PostMapping("/create/change")
    public Result comment(@RequestBody CommentParam commentParam)
    {
        return commentsService.comment(commentParam);
    }



}
