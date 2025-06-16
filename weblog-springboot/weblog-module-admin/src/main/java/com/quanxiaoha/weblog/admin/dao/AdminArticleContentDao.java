package com.quanxiaoha.weblog.admin.dao;

import com.quanxiaoha.weblog.common.domain.dos.ArticleContentDO;

/**
 * 文章内容数据访问接口
 * 处理文章内容的新增、查询、更新和删除操作
 */
public interface AdminArticleContentDao {
    /**
     * 新增文章内容
     * @param articleContentDO 文章内容数据对象
     * @return 影响行数
     */
    int insertArticleContent(ArticleContentDO articleContentDO);

    /**
     * 根据文章ID查询内容
     * @param articleId 文章ID
     * @return 文章内容数据对象
     */
    ArticleContentDO queryByArticleId(Long articleId);

    /**
     * 根据文章ID删除内容
     * @param articleId 文章ID
     * @return 影响行数
     */
    int deleteByArticleId(Long articleId);

    /**
     * 根据文章ID更新内容
     * @param articleContentDO 文章内容数据对象
     * @return 影响行数
     */
    int updateByArticleId(ArticleContentDO articleContentDO);
}
