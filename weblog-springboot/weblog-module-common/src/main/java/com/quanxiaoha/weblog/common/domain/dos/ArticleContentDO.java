package com.quanxiaoha.weblog.common.domain.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文章内容数据对象
 * 映射数据库表：t_article_content
 */
@Data
@Builder
@TableName("t_article_content")
public class ArticleContentDO {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 文章ID
     */
    private Long articleId;
    
    /**
     * 文章内容
     */
    private String content;
}
