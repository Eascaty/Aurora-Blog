package com.aurora.blog.service.impl;

import com.aurora.blog.dao.mapper.TagMapper;
import com.aurora.blog.dao.pojo.Category;
import com.aurora.blog.dao.pojo.Tag;
import com.aurora.blog.service.TagService;
import com.aurora.blog.vo.Result;
import com.aurora.blog.vo.TagVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagMapper tagMapper;



    public TagVo copy(Tag tag){
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(tag,tagVo);
        return tagVo;
    }
    public List<TagVo> copyList(List<Tag> tagList){
        List<TagVo> tagVoList = new ArrayList<>();
        for (Tag tag : tagList) {
            tagVoList.add(copy(tag));
        }
        return tagVoList;
    }

    @Override
    public List<TagVo> findTagsByArticleId(Long id) {
        List<Tag> tags = tagMapper.findTagsByArticleId(id);
        return copyList(tags);
    }

    /**
     * 1.标签所拥有的的文章数量最多 最热标签
     * 2.查询 根据tag_id 分组 计数,从大到小排列 取前Limit个
     * @param limit
     * @return
     */
    @Override
    public Result hots(int limit) {
       List<Long> tagIds = tagMapper.findHotsTagIds(limit);
        if(CollectionUtils.isEmpty(tagIds)){
            return Result.success(Collections.emptyList());
        }
//        需求的是 tagId 和 tagName Tag对象
//        select * from tag where id in (1,2,3,4)
        List<Tag> tagList = tagMapper.findTagsByTagIds(tagIds);
        return Result.success(tagList);
    }
    @Override
    public Result findAll() {
        List<Tag> tags = this.tagMapper.selectList(new LambdaQueryWrapper<>());
        return Result.success(copyList(tags));
    }

    @Override
    public Result findAllDetail() {
        List<Tag> tags = tagMapper.selectList(new LambdaQueryWrapper<>());
//        页面交互对象
        return Result.success(copyList(tags));
    }

}
