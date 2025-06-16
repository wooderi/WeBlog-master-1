package com.quanxiaoha.weblog.admin.controller;

import com.quanxiaoha.weblog.admin.model.vo.category.AddCategoryReqVO;
import com.quanxiaoha.weblog.admin.model.vo.category.DeleteCategoryReqVO;
import com.quanxiaoha.weblog.admin.model.vo.category.QueryCategoryPageListReqVO;
import com.quanxiaoha.weblog.admin.service.AdminCategoryService;
import com.quanxiaoha.weblog.common.PageResponse;
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
 * 分类管理控制器
 * 处理分类的新增、查询、删除等操作
 */
@RestController
@RequestMapping("/admin/category")
public class AdminCategoryController {

    @Autowired
    private AdminCategoryService categoryService;

    /**
     * 新增分类
     * @param addCategoryReqVO 分类新增请求参数，包含分类名称等信息
     * @return 新增结果，成功返回成功信息，失败返回错误信息
     */
    @PostMapping("/add")
    @ApiOperationLog(description = "新增分类")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Response addCategory(@RequestBody @Validated AddCategoryReqVO addCategoryReqVO) {
        return categoryService.addCategory(addCategoryReqVO);
    }

    /**
     * 获取分类列表分页数据
     * @param queryCategoryPageListReqVO 分类分页查询请求参数，包含页码、每页条数等
     * @return 分类分页列表，包含分类基本信息
     */
    @PostMapping("/list")
    @ApiOperationLog(description = "获取分类列表分页数据")
    public PageResponse queryCategoryPageList(@RequestBody QueryCategoryPageListReqVO queryCategoryPageListReqVO) {
        return categoryService.queryCategoryPageList(queryCategoryPageListReqVO);
    }

    /**
     * 删除分类
     * @param deleteCategoryReqVO 分类删除请求参数，包含分类ID
     * @return 删除结果，成功返回成功信息，失败返回错误信息
     */
    @PostMapping("/delete")
    @ApiOperationLog(description = "删除分类")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Response deleteCategory(@RequestBody @Validated DeleteCategoryReqVO deleteCategoryReqVO) {
        return categoryService.deleteCategory(deleteCategoryReqVO);
    }

    /**
     * 获取所有分类下拉框数据
     * @return 分类下拉框列表，包含分类ID和名称
     */
    @PostMapping("/select/list")
    @ApiOperationLog(description = "获取所有分类下拉框数据")
    public Response queryCategorySelectList() {
        return categoryService.queryCategorySelectList();
    }
}
