package com.quanxiaoha.weblog.admin.dao;

import com.quanxiaoha.weblog.common.domain.dos.ArticleTagRelDO;

import java.util.List;

/**
 * 文章标签关联数据访问接口
 * 处理文章与标签之间的关联关系
 */
public interface AdminArticleTagRelDao {
    /**
     * 批量新增文章标签关联关系
     * @param articleTagRelDOS 文章标签关联数据对象列表
     */
    void insertBatch(List<ArticleTagRelDO> articleTagRelDOS);

    /**
     * 根据文章ID查询标签关联列表
     * @param articleId 文章ID
     * @return 文章标签关联数据对象列表
     */
    List<ArticleTagRelDO> selectByArticleId(Long articleId);

    /**
     * 根据文章ID删除标签关联
     * @param articleId 文章ID
     * @return 影响行数
     */
    int deleteByArticleId(Long articleId);
}
