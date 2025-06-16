package com.quanxiaoha.weblog.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quanxiaoha.weblog.admin.dao.AdminCategoryDao;
import com.quanxiaoha.weblog.common.domain.dos.ArticleDO;
import com.quanxiaoha.weblog.common.domain.mapper.CategoryMapper;
import com.quanxiaoha.weblog.common.domain.dos.CategoryDO;
import com.quanxiaoha.weblog.admin.model.vo.category.AddCategoryReqVO;
import com.quanxiaoha.weblog.admin.model.vo.category.DeleteCategoryReqVO;
import com.quanxiaoha.weblog.admin.model.vo.category.QueryCategoryPageListReqVO;
import com.quanxiaoha.weblog.admin.model.vo.category.QueryCategoryPageListRspVO;
import com.quanxiaoha.weblog.admin.service.AdminCategoryService;
import com.quanxiaoha.weblog.common.PageResponse;
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
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@Slf4j
/**
 * 分类管理服务实现类
 * 负责分类的添加、查询、删除等操作
 */
public class AdminCategoryServiceImpl extends ServiceImpl<CategoryMapper, CategoryDO> implements AdminCategoryService {

    @Autowired
    private AdminCategoryDao categoryDao;

    /**
     * 添加分类
     * @param addCategoryReqVO 分类添加请求参数，包含分类名称
     * @return 添加结果，成功返回success，分类已存在返回DUPLICATE_CATEGORY_ERROR错误
     */
    @Override
    public Response addCategory(AddCategoryReqVO addCategoryReqVO) {
        String categoryName = addCategoryReqVO.getName();
        try {
            CategoryDO categoryDO = CategoryDO.builder()
                    .name(categoryName.trim())
                    .createTime(new Date())
                    .updateTime(new Date())
                    .build();
            save(categoryDO);
            return Response.success();
        } catch (DuplicateKeyException e) {
            log.error("==> 该分类已经存在：{}", categoryName, e);
            return Response.fail(ResponseCodeEnum.DUPLICATE_CATEGORY_ERROR);
        }

    }

    /**
     * 分页查询分类列表
     * @param queryCategoryPageListReqVO 分类分页查询请求参数，包含当前页、每页大小、分类名称、开始日期、结束日期等信息
     * @return 分页分类列表数据
     */
    @Override
    public PageResponse queryCategoryPageList(QueryCategoryPageListReqVO queryCategoryPageListReqVO) {
        Long current = queryCategoryPageListReqVO.getCurrent();
        Long size = queryCategoryPageListReqVO.getSize();
        Page<CategoryDO> page = new Page<>(current, size);
        QueryWrapper<CategoryDO> wrapper = new QueryWrapper<>();

        String categoryName = queryCategoryPageListReqVO.getCategoryName();
        Date startDate = queryCategoryPageListReqVO.getStartDate();
        Date endDate = queryCategoryPageListReqVO.getEndDate();

        wrapper.lambda()
                .like(Objects.nonNull(categoryName), CategoryDO::getName, categoryName)
                .ge(Objects.nonNull(startDate), CategoryDO::getCreateTime, startDate)
                .le(Objects.nonNull(endDate), CategoryDO::getCreateTime, endDate)
                .orderByDesc(CategoryDO::getCreateTime);

        Page<CategoryDO> categoryDOPage = page(page, wrapper);

        List<CategoryDO> categoryDOS = categoryDOPage.getRecords();

        List<QueryCategoryPageListRspVO> queryCategoryPageListRspVOS = null;
        if (!CollectionUtils.isEmpty(categoryDOS)) {
            queryCategoryPageListRspVOS = categoryDOS.stream()
                    .map(p -> QueryCategoryPageListRspVO.builder()
                            .id(p.getId())
                            .name(p.getName())
                            .createTime(p.getCreateTime())
                            .build())
                    .collect(Collectors.toList());

        }

        return PageResponse.success(categoryDOPage, queryCategoryPageListRspVOS);
    }

    /**
     * 删除分类
     * @param deleteCategoryReqVO 分类删除请求参数，包含分类ID
     * @return 删除结果，成功返回success
     */
    @Override
    public Response deleteCategory(DeleteCategoryReqVO deleteCategoryReqVO) {
        Long categoryId = deleteCategoryReqVO.getCategoryId();
        removeById(categoryId);
        return Response.success();
    }

    /**
     * 查询分类下拉列表
     * @return 分类下拉列表数据，包含分类ID和名称
     */
    @Override
    public Response queryCategorySelectList() {
        List<CategoryDO> categoryDOList = categoryDao.selectAllCategory();
        List<QuerySelectListRspVO> list = null;
        if (!CollectionUtils.isEmpty(categoryDOList)) {
            list = categoryDOList.stream()
                    .map(p -> QuerySelectListRspVO.builder()
                            .label(p.getName())
                            .value(p.getId())
                            .build())
                    .collect(Collectors.toList());
        }
        return Response.success(list);
    }
}
