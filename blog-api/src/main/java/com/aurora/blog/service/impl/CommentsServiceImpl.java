package com.aurora.blog.service.impl;

import com.aurora.blog.dao.mapper.CommentMapper;
import com.aurora.blog.dao.pojo.Comment;
import com.aurora.blog.dao.pojo.SysUser;
import com.aurora.blog.service.CommentsService;
import com.aurora.blog.service.SysUserService;
import com.aurora.blog.utils.UserThreadLocal;
import com.aurora.blog.vo.CommentVo;
import com.aurora.blog.vo.Result;
import com.aurora.blog.vo.UserVo;
import com.aurora.blog.vo.params.CommentParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentsServiceImpl  implements CommentsService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private SysUserService sysUserService;

    @Override
    public Result commentsByArticleId(Long id) {
        /**
         * 1.根据文章id 查询 评论列表 从comment 表中查询
         * 2.根据作者的id 查询作者的信息
         * 3.判断 如果 level = 1  要去查询它有没有子评论
         * 4.如果有 根据评论id 进行查询 （parent_id）
         */
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getLevel,1);
        queryWrapper.eq(Comment::getArticleId,id);
        List<Comment> comments = commentMapper.selectList(queryWrapper);

        return Result.success(copyList(comments));

    }

    private List<CommentVo> copyList(List<Comment> comments) {
        List<CommentVo> commentVoList = new ArrayList<>();
        for(Comment comment : comments) {
            commentVoList.add(copy(comment));
        }
        return commentVoList ;
    }

    private CommentVo copy(Comment comment) {
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment, commentVo);
        commentVo.setCreateDate(new DateTime(comment.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
//       作者信息

        Long authorId = comment.getAuthorId();
        UserVo userVo = this.sysUserService.findUserVoById(authorId);
        commentVo.setAuthor(userVo);
//      子评论
        Integer level = comment.getLevel();
        if(1 == level){
            Long id = comment.getId();
            List<CommentVo> commentVoList = findCommentsByParentId(id);
            commentVo.setChildrens(commentVoList);
        }
//        to user 给谁评论
        if(level > 1){
            Long toUid = comment.getToUid();
            UserVo toUservo = this.sysUserService.findUserVoById(toUid);
            commentVo.setToUser(toUservo);
        }
        return commentVo;
    }

    private List<CommentVo> findCommentsByParentId(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getParentId,id);
        queryWrapper.eq(Comment::getLevel,2);

        return copyList(commentMapper.selectList(queryWrapper));
    }

    /**
     * 评论
     * @param commentParam
     * @return
     */
    @Override
    public Result comment(CommentParam commentParam) {
        SysUser sysUser = UserThreadLocal.get();
        Comment comment = new Comment();
        comment.setArticleId(commentParam.getArticleId());
        comment.setAuthorId(sysUser.getId());
        comment.setContent(commentParam.getContent());
        comment.setCreateDate(System.currentTimeMillis());
        Long parent = commentParam.getParent();
        if (parent == null || parent == 0) {
            comment.setLevel(1);
        }else{
            comment.setLevel(2);
        }
        comment.setParentId(parent == null ? 0 : parent);
        Long toUserId = commentParam.getToUserId();
        comment.setToUid(toUserId == null ? 0 : toUserId);
        this.commentMapper.insert(comment);
        return Result.success(null);
    }




}
