package com.quanxiaoha.weblog.admin.service;

import com.quanxiaoha.weblog.admin.model.vo.article.*;
import com.quanxiaoha.weblog.common.Response;

import java.util.List;

/**
 * 管理员文章服务接口，提供文章的发布、查询、更新、删除等核心操作
 */
public interface AdminArticleService {
    /**
     * 发布新文章
     * @param publishArticleReqVO 文章发布请求参数，包含标题、内容、标签等信息
     * @return 发布结果响应
     */
    Response publishArticle(PublishArticleReqVO publishArticleReqVO);

    /**
     * 查询文章详情
     * @param queryArticleDetailReqVO 文章详情查询参数，包含文章ID
     * @return 文章详情响应
     */
    Response queryArticleDetail(QueryArticleDetailReqVO queryArticleDetailReqVO);

    /**
     * 分页查询文章列表
     * @param queryArticlePageListReqVO 文章分页查询参数，包含页码、页大小、搜索条件等
     * @return 分页文章列表响应
     */
    Response queryArticlePageList(QueryArticlePageListReqVO queryArticlePageListReqVO);

    /**
     * 删除文章
     * @param deleteArticleReqVO 文章删除请求参数，包含文章ID
     * @return 删除结果响应
     */
    Response deleteArticle(DeleteArticleReqVO deleteArticleReqVO);

    /**
     * 更新文章
     * @param updateArticleReqVO 文章更新请求参数，包含文章ID及更新内容
     * @return 更新结果响应
     */
    Response updateArticle(UpdateArticleReqVO updateArticleReqVO);

}
