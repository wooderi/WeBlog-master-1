package com.quanxiaoha.weblog.common.domain.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * 文章标签数据对象
 * 映射数据库表：t_tag
 */
@Data
@Builder
@TableName("t_tag")
public class TagDO {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 标签名称
     */
    private String name;
    
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
}
