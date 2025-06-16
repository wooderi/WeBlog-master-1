package com.quanxiaoha.weblog.admin.model.vo.blogsetting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 查询博客设置响应VO
 * 包含博客ID、博客名称、作者、头像、介绍、GitHub主页、CSDN主页、Gitee主页和知乎主页等信息
 */ 
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryBlogSettingRspVO {
    private Long id;
    private String blogName;
    private String author;
    private String avatar;
    private String introduction;
    private String githubHome;
    private String csdnHome;
    private String giteeHome;
    private String zhihuHome;
}

