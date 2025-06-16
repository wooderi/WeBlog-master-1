package com.quanxiaoha.weblog.admin.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.quanxiaoha.weblog.admin.dao.AdminArticleCategoryRelDao;
import com.quanxiaoha.weblog.common.domain.mapper.ArticleCategoryRelMapper;
import com.quanxiaoha.weblog.common.domain.dos.ArticleCategoryRelDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 文章分类关联数据访问实现类
 * 实现文章与分类之间关联关系的数据库操作
 */
@Service
@Slf4j
public class AdminArticleCategoryRelDaoImpl implements AdminArticleCategoryRelDao {
    @Autowired
    private ArticleCategoryRelMapper articleCategoryRelMapper;

    /**
     * 新增文章分类关联关系
     * @param articleCategoryRelDO 文章分类关联数据对象
     * @return 影响行数
     */
    @Override
    public int insert(ArticleCategoryRelDO articleCategoryRelDO) {
        return articleCategoryRelMapper.insert(articleCategoryRelDO);
    }

    /**
     * 根据文章ID查询分类关联
     * @param articleId 文章ID
     * @return 文章分类关联数据对象
     */
    @Override
    public ArticleCategoryRelDO selectByArticleId(Long articleId) {
        QueryWrapper<ArticleCategoryRelDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ArticleCategoryRelDO::getArticleId, articleId);
        return articleCategoryRelMapper.selectOne(wrapper);
    }

    /**
     * 根据文章ID删除分类关联
     * @param articleId 文章ID
     * @return 影响行数
     */
    @Override
    public int deleteByArticleId(Long articleId) {
        QueryWrapper<ArticleCategoryRelDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ArticleCategoryRelDO::getArticleId, articleId);
        return articleCategoryRelMapper.delete(wrapper);
    }
}
