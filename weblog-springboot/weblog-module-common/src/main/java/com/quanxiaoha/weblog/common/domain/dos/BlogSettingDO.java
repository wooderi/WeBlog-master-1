package com.quanxiaoha.weblog.common.domain.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * 博客设置数据对象
 * 映射数据库表：t_blog_setting
 */
@Data
@Builder
@TableName("t_blog_setting")
public class BlogSettingDO {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 博客名称
     */
    private String blogName;
    
    /**
     * 作者名称
     */
    private String author;
    
    /**
     * 头像URL
     */
    private String avatar;
    
    /**
     * 个人简介
     */
    private String introduction;
    
    /**
     * GitHub主页URL
     */
    private String githubHome;
    
    /**
     * CSDN主页URL
     */
    private String csdnHome;
    
    /**
     * Gitee主页URL
     */
    private String giteeHome;
    
    /**
     * 知乎主页URL
     */
    private String zhihuHome;
}
