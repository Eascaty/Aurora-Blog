package com.aurora.blog.service.impl;


import com.aurora.blog.dao.dos.Archives;

//import com.aurora.blog.dao.mapper.TagMapper;
import com.aurora.blog.dao.mapper.ArticleBodyMapper;
import com.aurora.blog.dao.mapper.ArticleMapper;
import com.aurora.blog.dao.mapper.ArticleTagMapper;
import com.aurora.blog.dao.pojo.Article;

import com.aurora.blog.dao.pojo.ArticleBody;
import com.aurora.blog.dao.pojo.ArticleTag;
import com.aurora.blog.dao.pojo.SysUser;
import com.aurora.blog.service.*;
import com.aurora.blog.utils.UserThreadLocal;
import com.aurora.blog.vo.*;

import com.aurora.blog.vo.params.ArticleParam;
import com.aurora.blog.vo.params.PageParams;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private TagService tagService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ArticleTagMapper articleTagMapper;


    @Override
    public Result listArticle(PageParams pageparams) {
        Page<Article> page = new Page<Article>(pageparams.getPage(), pageparams.getPageSize());
        IPage<Article> articleIPage = articleMapper.listArticle(
                page,
                pageparams.getCategoryId(),
                pageparams.getTagId(),
                pageparams.getYear(),
                pageparams.getMonth());
        List<Article> records = articleIPage.getRecords();
        return Result.success(copyList(records,true,true));


    }


/*
    @Override
    public Result listArticle(PageParams pageparams) {
        */

    /**
     * 1.分页查询 article数据库表 测试
     *//*

        Page<Article> page = new Page<Article>(pageparams.getPage(), pageparams.getPageSize());
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        if (pageparams.getCategoryId() !=null){
//             and category_id=#{categoryId}
            queryWrapper.eq(Article::getCategoryId,pageparams.getCategoryId());
        }

        List<Long> articleIdList = new ArrayList<>();
        if (pageparams.getTagId() !=null){
//             加入标签  条件查询
//              article表中 并没有tag字段  一篇文章 有多个标签
//              article_tag  article_id  1： n   tag_id
            LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
            articleTagLambdaQueryWrapper.eq(ArticleTag::getTagId, pageparams.getTagId());
            List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagLambdaQueryWrapper);
            for (ArticleTag articleTag : articleTags) {
                articleIdList.add(articleTag.getArticleId());
            }
            if(articleIdList.size() > 0 ){
//              and id in(1,2,3)
                queryWrapper.in(Article::getId,articleIdList);

            }

        }

//        是否置顶排序
//        order by creat_date desc

        queryWrapper.orderByDesc(Article::getWeight, Article::getCreateDate);
        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
        List<Article> records = articlePage.getRecords();
//        能直接返回吗？ 很明显不能

        List<ArticleVo> articleVoList = copyList(records, true, true);
        return Result.success(articleVoList);
    }
*/
    @Override
    public Result hotArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getId, Article::getTitle);
        queryWrapper.last("limit " + limit);
//        select  id,title from article order by viewcounts desc limit
        List<Article> articles = articleMapper.selectList(queryWrapper);

        return Result.success(copyList(articles, false, false));
    }

    @Override
    public Result newArticles(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.select(Article::getId, Article::getTitle);
        queryWrapper.last("limit " + limit);
//        select  id,title f
//
//
//
//        rom article order by create_date desc limit
        List<Article> articles = articleMapper.selectList(queryWrapper);

        return Result.success(copyList(articles, false, false));
    }

    @Override
    public Result listArchives() {
        List<Archives> archivesList = articleMapper.listArchives();
        return Result.success(archivesList);
    }

    @Autowired
    private ThreadService threadService;


    @Override
    public ArticleVo findArticleById(Long id) {
        Article article = articleMapper.selectById(id);
        ArticleVo articleVo = copy(article, true, true, true, true);
//        查看完文章了，新增閱讀數，有沒有問題？
//        查看完文章後，本應該直接返回數據了，這時候做了一個更新操作，更新時加寫鎖，阻塞其他的讀操作，性能就會比較低。
//        更新 增加了此次接口的耗時 如果一旦更新出問題，不能影響 查看文章的操作
//        線程池 可以把更新操作 扔到線程池中去執行，和主線程就不相關了。
        threadService.updateArticleViewCount(articleMapper, article);
        return articleVo;

    }

    @Override
    @Transactional
    public Result publish(ArticleParam articleParam) {
//        此接口 要加入到登錄攔截中
        SysUser sysUser = UserThreadLocal.get();

        /**
         *  1.發佈文章  目的 構建 Article對象
         *  2.作者id  當前的登錄用戶
         *  3.標籤  要將標籤加入到關聯列表當中
         *  4.body  內容存儲
         */
        Article article = new Article();
        article.setAuthorId(sysUser.getId());
        article.setCategoryId(articleParam.getCategory().getId());
        article.setCreateDate(System.currentTimeMillis());
        article.setCommentCounts(0);
        article.setSummary(articleParam.getSummary());
        article.setTitle(articleParam.getTitle());
        article.setViewCounts(0);
        article.setWeight(Article.Article_Common);
        article.setBodyId(-1L);
//        插入之後，會生成一個文章Id
        this.articleMapper.insert(article);


        //tags
        List<TagVo> tags = articleParam.getTags();
        if (tags != null) {
            for (TagVo tag : tags) {

                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(tag.getId());
                this.articleTagMapper.insert(articleTag);
            }
        }
//        body
        ArticleBody articleBody = new ArticleBody();
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBody.setArticleId(article.getId());
        articleBodyMapper.insert(articleBody);

        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(article.getId());
        return Result.success(articleVo);
    }


    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record, isTag, isAuthor, false, false));
        }

        return articleVoList;
    }


    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record, isTag, isAuthor, isBody, isCategory));
        }

        return articleVoList;
    }

    @Autowired
    private CategoryService categoryService;


    private ArticleVo copy(Article article, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article, articleVo);

        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        //并不是所有的接口 都需要标签 ，作者信息
        if (isTag) {
            Long articleId = article.getId();
            articleVo.setTags(tagService.findTagsByArticleId(articleId));
        }
        if (isAuthor) {
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname());
        }
        if (isBody) {
            ArticleBodyVo articleBody = findArticleBody(article.getId());
            articleVo.setBody(articleBody);
        }
        if (isCategory) {
            CategoryVo categoryVo = findCategory(article.getCategoryId());
            articleVo.setCategory(categoryVo);
        }
        return articleVo;
    }


    private CategoryVo findCategory(Long categoryId) {
        return categoryService.findCategoryById(categoryId);
    }

    @Autowired
    private ArticleBodyMapper articleBodyMapper;

    private ArticleBodyVo findArticleBody(Long articleId) {
        LambdaQueryWrapper<ArticleBody> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleBody::getArticleId, articleId);
        ArticleBody articleBody = articleBodyMapper.selectOne(queryWrapper);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }

}

