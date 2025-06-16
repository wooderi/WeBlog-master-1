package com.quanxiaoha.weblog.common.domain.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * 文章统计数据对象
 * 用于存储按日期统计的文章数量信息
 */
@Data
@Builder
public class ArticleCountDO {
    /**
     * 统计日期
     */
    private String date;
    
    /**
     * 文章数量
     */
    private Long count;
}
