package com.quanxiaoha.weblog.admin.dao;

import com.quanxiaoha.weblog.common.domain.dos.CategoryDO;

import java.util.List;

/**
 * 分类数据访问接口
 * 处理分类信息的查询操作
 */
public interface AdminCategoryDao {
    /**
     * 查询所有分类
     * @return 分类数据对象列表
     */
    List<CategoryDO> selectAllCategory();

    /**
     * 查询分类总数
     * @return 分类总数
     */
    Long selectTotalCount();
}
