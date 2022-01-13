package com.aurora.blog.service.impl;


import com.aurora.blog.dao.mapper.ArticleMapper;
import com.aurora.blog.dao.pojo.Article;
import com.aurora.blog.service.ArticleService;
import com.aurora.blog.vo.ArticleVo;
import com.aurora.blog.vo.Result;
import com.aurora.blog.vo.params.PageParams;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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

    @Override
    public Result listArticle(PageParams pageparams) {
        /**
         * 1.分页查询 article数据库表 测试
         */
        Page<Article> page = new Page<Article>(pageparams.getPage(),pageparams.getPageSize());
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
//        是否置顶排序
//        order by creat_date desc

        queryWrapper.orderByDesc(Article::getWeight,Article::getCreateDate);
        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
        List<Article> records = articlePage.getRecords();
//        能直接返回吗？ 很明显不能

        List<ArticleVo> articleVoList = copyList(records,true,true);
        return Result.success(articleVoList);
    }

    @Override
    public Result hotArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit"+limit);
//        select  id,title from article order by viewcounts desc limit
        List<Article> articles = articleMapper.selectList(queryWrapper);

        return Result.success(copyList(articles,false,false));
    }

    private List<ArticleVo> copyList(List<Article> records,boolean isTag,boolean isAuthor) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for(Article record : records) {
            articleVoList.add(copy(record,isTag,isAuthor));
        }

        return articleVoList;
    }

    private ArticleVo copy(Article article){
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article,articleVo);

        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        return articleVo;
    }

}
