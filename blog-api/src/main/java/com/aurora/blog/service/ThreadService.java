package com.aurora.blog.service;

import com.aurora.blog.dao.mapper.ArticleMapper;
import com.aurora.blog.dao.pojo.Article;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class ThreadService {


    //    期望此操作在線程池 執行 不會影響原有的主線程
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article) {
        try {
            Thread.sleep(5000);
            System.out.println("更新完成了...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
