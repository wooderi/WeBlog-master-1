package com.quanxiaoha.weblog.common.domain.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * 文章访问量统计数据对象
 * 映射数据库表：t_statistics_article_pv
 */
@Data
@Builder
@TableName("t_statistics_article_pv")
public class StatisticsArticlePVDO {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 统计日期
     */
    private Date pvDate;
    
    /**
     * 访问量计数
     */
    private Long pvCount;
    
    /**
     * 记录创建时间
     */
    private Date createTime;
}
