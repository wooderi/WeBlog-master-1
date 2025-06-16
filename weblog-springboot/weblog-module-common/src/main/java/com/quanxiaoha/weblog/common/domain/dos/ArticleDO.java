package com.quanxiaoha.weblog.common.domain.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 文章数据对象
 * 映射数据库表：t_article
 */
@Data
@Builder
@TableName("t_article")
public class ArticleDO {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 文章标题
     */
    private String title;
    
    /**
     * 标题图片
     */
    private String titleImage;
    
    /**
     * 文章描述
     */
    private String description;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 更新时间
     */
    private Date updateTime;
    
    /**
     * 是否删除（0-未删除，1-已删除）
     */
    private Boolean isDeleted;
    
    /**
     * 阅读数量
     */
    private Long readNum;
}
