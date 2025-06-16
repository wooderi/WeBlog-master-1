package com.quanxiaoha.weblog.common.domain.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

/**
 * 文章标签关联关系数据对象
 * 映射数据库表：t_article_tag_rel
 */
@Data
@Builder
@TableName("t_article_tag_rel")
public class ArticleTagRelDO {
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
     * 标签ID
     */
    private Long tagId;
}
