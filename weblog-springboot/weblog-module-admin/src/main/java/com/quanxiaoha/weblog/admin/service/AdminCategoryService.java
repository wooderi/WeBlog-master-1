package com.quanxiaoha.weblog.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quanxiaoha.weblog.common.domain.dos.CategoryDO;
import com.quanxiaoha.weblog.admin.model.vo.category.AddCategoryReqVO;
import com.quanxiaoha.weblog.admin.model.vo.category.DeleteCategoryReqVO;
import com.quanxiaoha.weblog.admin.model.vo.category.QueryCategoryPageListReqVO;
import com.quanxiaoha.weblog.common.PageResponse;
import com.quanxiaoha.weblog.common.Response;


/**
 * 文章分类管理服务接口
 * 负责分类的创建、查询、删除等操作
 */
public interface AdminCategoryService extends IService<CategoryDO> {
    /**
     * 添加文章分类
     * @param addCategoryReqVO 分类添加请求参数
     * @return 操作结果
     */
    Response addCategory(AddCategoryReqVO addCategoryReqVO);

    /**
     * 分页查询分类列表
     * @param queryCategoryPageListReqVO 分页查询请求参数
     * @return 分页分类列表响应
     */
    PageResponse queryCategoryPageList(QueryCategoryPageListReqVO queryCategoryPageListReqVO);

    /**
     * 删除文章分类
     * @param deleteCategoryReqVO 分类删除请求参数
     * @return 操作结果
     */
    Response deleteCategory(DeleteCategoryReqVO deleteCategoryReqVO);

    /**
     * 查询分类下拉列表
     * @return 分类下拉列表响应
     */
    Response queryCategorySelectList();
}
