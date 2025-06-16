package com.quanxiaoha.weblog.web.convert;

import com.quanxiaoha.weblog.common.domain.dos.BlogSettingDO;
import com.quanxiaoha.weblog.web.model.vo.blogsetting.QueryBlogSettingRspVO;
import org.springframework.stereotype.Component;

/**
 * @author: 犬小哈
 * @url: www.quanxiaoha.com
 * @date: 2025-06-08 1:25
 * @description: 博客设置转换器实现类
 **/
@Component
public class BlogSettingConvertImpl implements BlogSettingConvert {

    /**
     * 将BlogSettingDO转换为QueryBlogSettingRspVO
     * 
     * @param bean 博客设置DO对象
     * @return 博客设置响应VO对象
     */
    @Override
    public QueryBlogSettingRspVO convert(BlogSettingDO bean) {
        if (bean == null) {
            return null;
        }

        return QueryBlogSettingRspVO.builder()
                .blogName(bean.getBlogName())
                .author(bean.getAuthor())
                .avatar(bean.getAvatar())
                .introduction(bean.getIntroduction())
                .githubHome(bean.getGithubHome())
                .csdnHome(bean.getCsdnHome())
                .giteeHome(bean.getGiteeHome())
                .zhihuHome(bean.getZhihuHome())
                .build();
    }
}