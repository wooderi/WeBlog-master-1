package com.quanxiaoha.weblog.common.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quanxiaoha.weblog.common.domain.dos.ArticleCountDO;
import com.quanxiaoha.weblog.common.domain.dos.ArticleDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 文章数据访问层
 * 提供文章基本CRUD操作及自定义统计查询
 */
public interface ArticleMapper extends BaseMapper<ArticleDO> {

    /**
     * 按日期统计文章数量
     * @param startDate 开始日期（yyyy-MM-dd）
     * @param endDate 结束日期（yyyy-MM-dd）
     * @return 日期-数量统计列表
     */
    @Select("SELECT DATE(create_time) AS date, COUNT(*) AS count\n" +
            "FROM t_article\n" +
            "WHERE create_time >= #{startDate} AND create_time <= #{endDate}\n" +
            "GROUP BY DATE(create_time)\n" +
            "ORDER BY DATE(create_time)")
    List<ArticleCountDO> selectArticleCount(@Param("startDate") String startDate, @Param("endDate") String endDate);
}
