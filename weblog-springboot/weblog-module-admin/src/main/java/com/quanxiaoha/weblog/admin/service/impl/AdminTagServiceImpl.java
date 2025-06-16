package com.quanxiaoha.weblog.admin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quanxiaoha.weblog.admin.dao.AdminTagDao;
import com.quanxiaoha.weblog.admin.model.vo.tag.SearchTagReqVO;
import com.quanxiaoha.weblog.common.domain.mapper.TagMapper;
import com.quanxiaoha.weblog.common.domain.dos.TagDO;
import com.quanxiaoha.weblog.admin.model.vo.tag.AddTagReqVO;
import com.quanxiaoha.weblog.admin.model.vo.tag.DeleteTagReqVO;
import com.quanxiaoha.weblog.admin.model.vo.tag.QueryTagPageListReqVO;
import com.quanxiaoha.weblog.admin.service.AdminTagService;
import com.quanxiaoha.weblog.common.Response;
import com.quanxiaoha.weblog.common.enums.ResponseCodeEnum;
import com.quanxiaoha.weblog.common.model.vo.QuerySelectListRspVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
/**
 * 标签管理服务实现类
 * 负责标签的添加、查询、删除等操作
 */
public class AdminTagServiceImpl extends ServiceImpl<TagMapper, TagDO> implements AdminTagService {

    @Autowired
    private AdminTagDao tagDao;

    /**
     * 批量添加标签
     * @param addTagReqVO 标签添加请求参数，包含标签名称列表
     * @return 操作结果，成功返回success；若存在重复标签名则返回错误信息
     */
    @Override
    public Response addTags(AddTagReqVO addTagReqVO) {
        List<TagDO> tags = addTagReqVO.getTags().stream()
                .map(tagName -> TagDO.builder().name(tagName.trim())
                        .createTime(new Date())
                        .updateTime(new Date())
                        .build()).collect(Collectors.toList());
        try {
            saveBatch(tags);
        } catch (DuplicateKeyException e) {
            log.error("==> 含有重复标签名", e);
            return Response.fail(String.format("%s: %s", ResponseCodeEnum.DUPLICATE_TAG_ERROR.getErrorMessage(), e.getMessage()));
        }
        return Response.success();
    }

    /**
     * 分页查询标签列表
     * @param queryTagPageListReqVO 分页查询请求参数，包含页码、每页大小、日期范围和标签名称筛选条件
     * @return 包含标签分页数据的响应对象
     */
    @Override
    public Response queryTagPageList(QueryTagPageListReqVO queryTagPageListReqVO) {
        Long current = queryTagPageListReqVO.getCurrent();
        Long size = queryTagPageListReqVO.getSize();
        Date startDate = queryTagPageListReqVO.getStartDate();
        Date endDate = queryTagPageListReqVO.getEndDate();
        String tagName = queryTagPageListReqVO.getTagName();

        Page<TagDO> tagDOPage = tagDao.queryTagPageList(current, size, startDate, endDate, tagName);

        return Response.success(tagDOPage);
    }

    /**
     * 删除标签
     * @param deleteTagReqVO 标签删除请求参数，包含标签ID
     * @return 操作结果，成功返回success
     */
    @Override
    public Response deleteTag(DeleteTagReqVO deleteTagReqVO) {
        removeById(deleteTagReqVO.getTagId());
        return Response.success();
    }

    /**
     * 搜索标签
     * @param searchTagReqVO 标签搜索请求参数，包含搜索关键词
     * @return 包含匹配标签的下拉选择列表响应
     */
    @Override
    public Response searchTags(SearchTagReqVO searchTagReqVO) {
        String key = searchTagReqVO.getKey();
        List<TagDO> tagDOS = tagDao.searchTags(key);
        List<QuerySelectListRspVO> selectListRspVOS = null;

        if (!CollectionUtils.isEmpty(tagDOS)) {
            selectListRspVOS = tagDOS.stream()
                    .map(p -> QuerySelectListRspVO.builder().label(p.getName()).value(p.getId()).build())
                    .collect(Collectors.toList());
        }
        return Response.success(selectListRspVOS);
    }

    /**
     * 查询所有标签的下拉选择列表
     * @return 包含所有标签的标签-值对列表响应
     */
    @Override
    public Response queryTagSelectList() {
        List<TagDO> tagDOS = tagDao.selectAll();
        List<QuerySelectListRspVO> list = null;
        if (!CollectionUtils.isEmpty(tagDOS)) {
            list = tagDOS.stream()
                    .map(p -> QuerySelectListRspVO.builder()
                            .label(p.getName())
                            .value(p.getId())
                            .build())
                    .collect(Collectors.toList());
        }
        return Response.success(list);
    }
}
