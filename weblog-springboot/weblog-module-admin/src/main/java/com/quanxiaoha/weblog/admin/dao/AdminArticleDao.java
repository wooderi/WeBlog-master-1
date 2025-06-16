/**
 * 管理员文章数据访问对象接口，提供对文章数据的各种操作方法。
 */
package com.quanxiaoha.weblog.admin.dao;

/**
 * 引入 MyBatis-Plus 的分页插件类，用于实现文章列表的分页查询。
 */
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
/**
 * 引入文章统计数据对象类，用于存储文章统计相关信息。
 */
import com.quanxiaoha.weblog.common.domain.dos.ArticleCountDO;
/**
 * 引入文章数据对象类，用于存储文章的基本信息。
 */
import com.quanxiaoha.weblog.common.domain.dos.ArticleDO;

import java.util.Date;
import java.util.List;

/**
 * 管理员文章数据访问对象接口，定义了对文章数据的增删改查以及统计等操作方法。
 */
public interface AdminArticleDao {
    /**
     * 插入一篇新文章到数据库。
     * @param articleDO 文章数据对象，包含文章的详细信息。
     * @return 插入操作影响的行数，成功插入返回 1，失败返回 0。
     */
    int insertArticle(ArticleDO articleDO);

    /**
     * 根据文章 ID 查询文章信息。
     * @param articleId 文章的唯一标识 ID。
     * @return 文章数据对象，如果未找到则返回 null。
     */
    ArticleDO queryByArticleId(Long articleId);

    /**
     * 分页查询文章列表。
     * @param current 当前页码。
     * @param size 每页显示的记录数。
     * @param startDate 开始日期，用于筛选文章创建时间。
     * @param endDate 结束日期，用于筛选文章创建时间。
     * @param searchTitle 搜索关键词，用于匹配文章标题。
     * @return 包含文章数据对象的分页对象。
     */
    Page<ArticleDO> queryArticlePageList(Long current, Long size, Date startDate, Date endDate, String searchTitle);

    /**
     * 根据文章 ID 删除文章。
     * @param articleId 文章的唯一标识 ID。
     * @return 删除操作影响的行数，成功删除返回 1，失败返回 0。
     */
    int deleteById(Long articleId);

    /**
     * 根据文章 ID 更新文章信息。
     * @param articleDO 文章数据对象，包含需要更新的文章信息。
     * @return 更新操作影响的行数，成功更新返回 1，失败返回 0。
     */
    int updateById(ArticleDO articleDO);

    /**
     * 查询文章的总数量。
     * @return 文章的总数量。
     */
    Long selectTotalCount();

    /**
     * 根据日期范围查询文章统计信息。
     * @param startDate 开始日期。
     * @param endDate 结束日期。
     * @return 包含文章统计数据对象的列表。
     */
    List<ArticleCountDO> selectArticleCount(String startDate, String endDate);

    /**
     * 增加指定文章的阅读量。
     * @param articleId 文章的唯一标识 ID。
     * @return 更新操作影响的行数，成功更新返回 1，失败返回 0。
     */
    int readNumIncrease(Long articleId);
}
