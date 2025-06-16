package com.quanxiaoha.weblog.admin.controller;

import com.quanxiaoha.weblog.admin.model.vo.tag.AddTagReqVO;
import com.quanxiaoha.weblog.admin.model.vo.tag.DeleteTagReqVO;
import com.quanxiaoha.weblog.admin.model.vo.tag.QueryTagPageListReqVO;
import com.quanxiaoha.weblog.admin.model.vo.tag.SearchTagReqVO;
import com.quanxiaoha.weblog.admin.service.AdminTagService;
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
 * 标签管理控制器
 * 处理标签的新增、查询、删除等操作
 */
@RestController
@RequestMapping("/admin/tag")
public class AdminTagController {

    @Autowired
    private AdminTagService tagService;

    /**
     * 新增标签
     * @param addTagReqVO 标签新增请求参数，包含标签名称等信息
     * @return 新增结果，成功返回成功信息，失败返回错误信息
     */
    @PostMapping("/add")
    @ApiOperationLog(description = "新增标签")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Response addTag(@RequestBody @Validated AddTagReqVO addTagReqVO) {
        return tagService.addTags(addTagReqVO);
    }

    /**
     * 获取标签列表分页数据
     * @param queryTagPageListReqVO 标签分页查询请求参数，包含页码、每页条数等
     * @return 标签分页列表，包含标签基本信息
     */
    @PostMapping("/list")
    @ApiOperationLog(description = "获取标签列表分页数据")
    public Response queryTagPageList(@RequestBody QueryTagPageListReqVO queryTagPageListReqVO) {
        return tagService.queryTagPageList(queryTagPageListReqVO);
    }

    /**
     * 删除标签
     * @param deleteTagReqVO 标签删除请求参数，包含标签ID
     * @return 删除结果，成功返回成功信息，失败返回错误信息
     */
    @PostMapping("/delete")
    @ApiOperationLog(description = "删除标签")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Response deleteTag(@RequestBody @Validated DeleteTagReqVO deleteTagReqVO) {
        return tagService.deleteTag(deleteTagReqVO);
    }

    @PostMapping("/search")
    @ApiOperationLog(description = "标签模糊查询")
    public Response searchTag(@RequestBody @Validated SearchTagReqVO searchTagReqVO) {
        return tagService.searchTags(searchTagReqVO);
    }

    /**
     * 获取所有标签下拉框数据
     * @return 标签下拉框列表，包含标签ID和名称
     */
    @PostMapping("/select/list")
    @ApiOperationLog(description = "获取所有标签下拉框数据")
    public Response queryTagSelectList() {
        return tagService.queryTagSelectList();
    }

}
