package com.quanxiaoha.weblog.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quanxiaoha.weblog.admin.model.vo.blogsetting.QueryBlogSettingRspVO;
import com.quanxiaoha.weblog.admin.model.vo.blogsetting.UpdateBlogSettingReqVO;
import com.quanxiaoha.weblog.admin.model.vo.user.QueryUserDetailRspVO;
import com.quanxiaoha.weblog.admin.service.AdminBlogSettingService;
import com.quanxiaoha.weblog.common.Response;
import com.quanxiaoha.weblog.common.domain.mapper.BlogSettingMapper;
import com.quanxiaoha.weblog.common.domain.dos.BlogSettingDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
@Slf4j
/**
 * 博客设置管理服务实现类
 * 负责博客基本信息的更新、查询等配置操作
 */
/**
 * 博客设置服务实现类
 * 负责博客基本信息的配置管理，包括更新设置和查询设置详情等操作
 */
public class AdminBlogSettingServiceImpl extends ServiceImpl<BlogSettingMapper, BlogSettingDO> implements AdminBlogSettingService {
    /**
     * 更新博客设置信息
     * @param updateBlogSettingReqVO 博客设置更新请求参数，包含博客名称、作者、头像等信息
     * @return 操作结果，成功返回success
     */
    @Override
    public Response updateBlogSetting(UpdateBlogSettingReqVO updateBlogSettingReqVO) {
        BlogSettingDO blogSettingDO = BlogSettingDO.builder()
                .id(1L)
                .blogName(updateBlogSettingReqVO.getBlogName())
                .author(updateBlogSettingReqVO.getAuthor())
                .avatar(updateBlogSettingReqVO.getAvatar())
                .introduction(updateBlogSettingReqVO.getIntroduction())
                .githubHome(updateBlogSettingReqVO.getGithubHome())
                .giteeHome(updateBlogSettingReqVO.getGiteeHome())
                .csdnHome(updateBlogSettingReqVO.getCsdnHome())
                .zhihuHome(updateBlogSettingReqVO.getZhihuHome())
                .build();
        saveOrUpdate(blogSettingDO);
        return Response.success();
    }

    /**
     * 查询博客设置详情
     * @return 博客设置详情响应，包含博客名称、作者、头像、个人介绍及各平台主页链接等信息
     */
    @Override
    public Response queryBlogSettingDetail() {
        BlogSettingDO blogSettingDO = getOne(null);

        QueryBlogSettingRspVO queryBlogSettingRspVO = null;
        if (Objects.nonNull(blogSettingDO)) {
            queryBlogSettingRspVO = QueryBlogSettingRspVO.builder()
                    .id(blogSettingDO.getId())
                    .blogName(blogSettingDO.getBlogName())
                    .author(blogSettingDO.getAuthor())
                    .avatar(blogSettingDO.getAvatar())
                    .introduction(blogSettingDO.getIntroduction())
                    .githubHome(blogSettingDO.getGithubHome())
                    .csdnHome(blogSettingDO.getCsdnHome())
                    .giteeHome(blogSettingDO.getGiteeHome())
                    .zhihuHome(blogSettingDO.getZhihuHome())
                    .build();
        }
        return Response.success(queryBlogSettingRspVO);
    }

    /**
     * 查询当前用户的昵称和头像
     * @return 用户详情响应，包含用户名和头像信息
     */
    @Override
    public Response<QueryUserDetailRspVO> queryNicknameAndAvatar() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        QueryUserDetailRspVO queryBlogSettingRspVO = QueryUserDetailRspVO.builder().username(username).build();

        BlogSettingDO blogSettingDO = getOne(null);
        queryBlogSettingRspVO.setAvatar(blogSettingDO.getAvatar());
        return Response.success(queryBlogSettingRspVO);
    }
}
