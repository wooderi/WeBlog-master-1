package com.quanxiaoha.weblog.common.domain.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * 文章分类关联关系数据对象
 * 映射数据库表：t_article_category_rel
 */
@Data
@Builder
@TableName("t_article_category_rel")
public class ArticleCategoryRelDO {
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
     * 分类ID
     */
    private Long categoryId;
}
