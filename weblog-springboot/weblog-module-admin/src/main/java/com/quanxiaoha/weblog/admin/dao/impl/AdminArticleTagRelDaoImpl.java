package com.quanxiaoha.weblog.admin.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.quanxiaoha.weblog.admin.dao.AdminArticleTagRelDao;
import com.quanxiaoha.weblog.common.domain.mapper.ArticleTagRelMapper;
import com.quanxiaoha.weblog.common.domain.dos.ArticleTagRelDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 文章标签关联数据访问实现类
 * 实现文章与标签之间关联关系的数据库操作
 */
@Service
@Slf4j
public class AdminArticleTagRelDaoImpl implements AdminArticleTagRelDao {
    @Autowired
    private ArticleTagRelMapper articleTagRelMapper;

    /**
     * 批量新增文章标签关联关系
     * @param articleTagRelDOS 文章标签关联数据对象列表
     */
    @Override
    public void insertBatch(List<ArticleTagRelDO> articleTagRelDOS) {
        articleTagRelMapper.insertBatchSomeColumn(articleTagRelDOS);
    }

    /**
     * 根据文章ID查询标签关联列表
     * @param articleId 文章ID
     * @return 文章标签关联数据对象列表
     */
    @Override
    public List<ArticleTagRelDO> selectByArticleId(Long articleId) {
        QueryWrapper<ArticleTagRelDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ArticleTagRelDO::getArticleId, articleId);
        return articleTagRelMapper.selectList(wrapper);
    }

    /**
     * 根据文章ID删除标签关联
     * @param articleId 文章ID
     * @return 影响行数
     */
    @Override
    public int deleteByArticleId(Long articleId) {
        QueryWrapper<ArticleTagRelDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ArticleTagRelDO::getArticleId, articleId);
        return articleTagRelMapper.delete(wrapper);
    }
}
