package com.quanxiaoha.weblog.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quanxiaoha.weblog.admin.model.vo.tag.SearchTagReqVO;
import com.quanxiaoha.weblog.common.domain.dos.TagDO;
import com.quanxiaoha.weblog.admin.model.vo.tag.AddTagReqVO;
import com.quanxiaoha.weblog.admin.model.vo.tag.DeleteTagReqVO;
import com.quanxiaoha.weblog.admin.model.vo.tag.QueryTagPageListReqVO;
import com.quanxiaoha.weblog.common.Response;


/**
 * 标签管理服务接口
 * 负责标签的创建、查询、删除、搜索等操作
 */
public interface AdminTagService extends IService<TagDO> {
    /**
     * 添加标签
     * @param addTagReqVO 标签添加请求参数
     * @return 操作结果
     */
    Response addTags(AddTagReqVO addTagReqVO);

    /**
     * 分页查询标签列表
     * @param queryTagPageListReqVO 分页查询请求参数
     * @return 分页标签列表响应
     */
    Response queryTagPageList(QueryTagPageListReqVO queryTagPageListReqVO);

    /**
     * 删除标签
     * @param deleteTagReqVO 标签删除请求参数
     * @return 操作结果
     */
    Response deleteTag(DeleteTagReqVO deleteTagReqVO);

    /**
     * 搜索标签
     * @param searchTagReqVO 标签搜索请求参数
     * @return 包含搜索结果的响应
     */
    Response searchTags(SearchTagReqVO searchTagReqVO);

    /**
     * 查询标签下拉列表
     * @return 标签下拉列表响应
     */
    Response queryTagSelectList();
}
