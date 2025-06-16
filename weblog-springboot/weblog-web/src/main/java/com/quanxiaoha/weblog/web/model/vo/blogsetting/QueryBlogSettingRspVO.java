package com.quanxiaoha.weblog.web.model.vo.blogsetting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryBlogSettingRspVO {
    private String blogName;
    private String author;
    private String avatar;
    private String introduction;
    private String githubHome;
    private String csdnHome;
    private String giteeHome;
    private String zhihuHome;
}

