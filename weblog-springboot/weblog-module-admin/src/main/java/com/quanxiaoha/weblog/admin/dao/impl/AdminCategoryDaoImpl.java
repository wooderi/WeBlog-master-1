package com.quanxiaoha.weblog.admin.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.quanxiaoha.weblog.admin.dao.AdminCategoryDao;
import com.quanxiaoha.weblog.common.domain.mapper.CategoryMapper;
import com.quanxiaoha.weblog.common.domain.dos.CategoryDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 分类数据访问实现类
 * 实现分类信息的查询和统计等数据库操作
 */
@Service
@Slf4j
public class AdminCategoryDaoImpl implements AdminCategoryDao {
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 查询所有分类
     * @return 分类数据对象列表
     */
    @Override
    public List<CategoryDO> selectAllCategory() {
        return categoryMapper.selectList(null);
    }

    /**
     * 查询分类总数（未删除状态）
     * @return 分类总数
     */
    @Override
    public Long selectTotalCount() {
        QueryWrapper<CategoryDO> wrapper = new QueryWrapper<>();
        wrapper.select("1").lambda().eq(CategoryDO::getIsDeleted, 0);
        return categoryMapper.selectCount(wrapper);
    }
}
