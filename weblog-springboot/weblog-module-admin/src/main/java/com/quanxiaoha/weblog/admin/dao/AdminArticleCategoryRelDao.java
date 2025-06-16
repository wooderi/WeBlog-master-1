package com.quanxiaoha.weblog.admin.dao;

import com.quanxiaoha.weblog.common.domain.dos.ArticleCategoryRelDO;

/**
 * 文章分类关联数据访问接口
 * 处理文章与分类之间的关联关系
 */
public interface AdminArticleCategoryRelDao {
    /**
     * 新增文章分类关联关系
     * @param articleCategoryRelDO 文章分类关联数据对象
     * @return 影响行数
     */
    int insert(ArticleCategoryRelDO articleCategoryRelDO);

    /**
     * 根据文章ID查询分类关联
     * @param articleId 文章ID
     * @return 文章分类关联数据对象
     */
    ArticleCategoryRelDO selectByArticleId(Long articleId);

    /**
     * 根据文章ID删除分类关联
     * @param articleId 文章ID
     * @return 影响行数
     */
    int deleteByArticleId(Long articleId);
}
