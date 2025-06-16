package com.quanxiaoha.weblog.admin.controller;

import com.quanxiaoha.weblog.admin.model.vo.article.*;
import com.quanxiaoha.weblog.admin.service.AdminArticleService;
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
 * 文章管理控制器
 * 处理文章的发布、查询、更新、删除等操作
 */
@RestController
@RequestMapping("/admin/article")
public class AdminArticleController {

    @Autowired
    private AdminArticleService articleService;

    /**
     * 发布文章
     * @param publishArticleReqVO 文章发布请求参数，包含标题、内容、分类ID、标签等信息
     * @return 发布结果，成功返回成功信息，失败返回错误信息
     */
    @PostMapping("/publish")
    @ApiOperationLog(description = "发布文章")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Response publishArticle(@RequestBody @Validated PublishArticleReqVO publishArticleReqVO) {
        return articleService.publishArticle(publishArticleReqVO);
    }

    /**
     * 修改文章
     * @param updateArticleReqVO 文章更新请求参数，包含文章ID、标题、内容、分类ID、标签等信息
     * @return 更新结果，成功返回成功信息，失败返回错误信息
     */
    @PostMapping("/update")
    @ApiOperationLog(description = "修改文章")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Response updateArticle(@RequestBody @Validated UpdateArticleReqVO updateArticleReqVO) {
        return articleService.updateArticle(updateArticleReqVO);
    }

    /**
     * 获取文章详情
     * @param queryArticleDetailReqVO 文章详情查询请求参数，包含文章ID
     * @return 文章详情信息，包含标题、内容、分类、标签等
     */
    @PostMapping("/detail")
    @ApiOperationLog(description = "获取文章详情")
    public Response queryArticleDetail(@RequestBody QueryArticleDetailReqVO queryArticleDetailReqVO) {
        return articleService.queryArticleDetail(queryArticleDetailReqVO);
    }

    /**
     * 获取文章分页数据
     * @param queryArticlePageListReqVO 文章分页查询请求参数，包含页码、每页条数、搜索关键词等
     * @return 文章分页列表，包含文章基本信息
     */
    @PostMapping("/list")
    @ApiOperationLog(description = "获取文章分页数据")
    public Response queryArticlePageList(@RequestBody QueryArticlePageListReqVO queryArticlePageListReqVO) {
        return articleService.queryArticlePageList(queryArticlePageListReqVO);
    }

    /**
     * 删除文章
     * @param deleteArticleReqVO 文章删除请求参数，包含文章ID
     * @return 删除结果，成功返回成功信息，失败返回错误信息
     */
    @PostMapping("/delete")
    @ApiOperationLog(description = "删除文章")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Response deleteArticle(@RequestBody @Validated DeleteArticleReqVO deleteArticleReqVO) {
        return articleService.deleteArticle(deleteArticleReqVO);
    }

}
