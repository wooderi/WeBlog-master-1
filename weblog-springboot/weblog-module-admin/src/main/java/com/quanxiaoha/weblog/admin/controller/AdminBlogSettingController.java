package com.quanxiaoha.weblog.admin.controller;

import com.quanxiaoha.weblog.admin.model.vo.article.*;
import com.quanxiaoha.weblog.admin.model.vo.blogsetting.UpdateBlogSettingReqVO;
import com.quanxiaoha.weblog.admin.service.AdminArticleService;
import com.quanxiaoha.weblog.admin.service.AdminBlogSettingService;
import com.quanxiaoha.weblog.common.Response;
import com.quanxiaoha.weblog.common.aspect.ApiOperationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 博客设置控制器
 * 处理博客基本信息的更新和查询操作
 */
@RestController
@RequestMapping("/admin/blog/setting")
public class AdminBlogSettingController {

    @Autowired
    private AdminBlogSettingService blogSettingService;

    /**
     * 更新博客设置信息
     * @param updateBlogSettingReqVO 博客设置更新请求参数，包含博客标题、描述、作者信息等
     * @return 更新结果，成功返回成功信息，失败返回错误信息
     */
    @PostMapping("/update")
    @ApiOperationLog(description = "更新博客设置信息")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Response updateBlogSetting(@RequestBody @Validated UpdateBlogSettingReqVO updateBlogSettingReqVO) {
        return blogSettingService.updateBlogSetting(updateBlogSettingReqVO);
    }

    /**
     * 获取博客设置详情信息
     * @return 博客设置详情，包含博客标题、描述、作者信息等
     */
    @PostMapping("/detail")
    @ApiOperationLog(description = "获取博客设置详情信息")
    public Response queryBlogSettingDetail() {
        return blogSettingService.queryBlogSettingDetail();
    }
}
