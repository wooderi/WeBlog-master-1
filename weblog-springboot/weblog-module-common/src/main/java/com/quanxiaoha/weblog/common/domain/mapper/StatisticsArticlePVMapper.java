package com.quanxiaoha.weblog.common.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quanxiaoha.weblog.common.domain.dos.ArticleCountDO;
import com.quanxiaoha.weblog.common.domain.dos.ArticleDO;
import com.quanxiaoha.weblog.common.domain.dos.StatisticsArticlePVDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 文章访问量统计数据访问层
 * 提供文章PV统计信息的CRUD操作
 */
public interface StatisticsArticlePVMapper extends BaseMapper<StatisticsArticlePVDO> {

}
