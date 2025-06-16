package com.quanxiaoha.weblog.admin.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quanxiaoha.weblog.admin.dao.AdminTagDao;
import com.quanxiaoha.weblog.common.domain.dos.ArticleDO;
import com.quanxiaoha.weblog.common.domain.mapper.TagMapper;
import com.quanxiaoha.weblog.common.domain.dos.TagDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;


/**
 * 标签数据访问实现类
 * 实现标签的分页查询、搜索、新增和统计等数据库操作
 */
@Service
@Slf4j
public class AdminTagDaoImpl implements AdminTagDao {
    @Autowired
    private TagMapper tagMapper;

    /**
     * 分页查询标签列表，支持时间范围和名称筛选
     * @param current 当前页码
     * @param size 每页大小
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param tagName 标签名称关键词
     * @return 分页标签数据对象
     */
    @Override
    public Page<TagDO> queryTagPageList(Long current, Long size, Date startDate, Date endDate, String tagName) {
        Page<TagDO> page = new Page<>(current, size);
        QueryWrapper<TagDO> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .like(Objects.nonNull(tagName), TagDO::getName, tagName)
                .ge(Objects.nonNull(startDate), TagDO::getCreateTime, startDate)
                .le(Objects.nonNull(endDate), TagDO::getCreateTime, endDate)
                .orderByDesc(TagDO::getCreateTime);
        return tagMapper.selectPage(page, wrapper);
    }

    /**
     * 按关键词搜索标签，忽略大小写
     * @param key 搜索关键词
     * @return 匹配的标签数据对象列表
     */
    @Override
    public List<TagDO> searchTags(String key) {
        // select * from t_tag where name like UPPER(CONCAT('%', 'J', '%'))
        // OR NAME LIKE LOWER(CONCAT('%', 'j', '%')) order by name
        QueryWrapper<TagDO> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .apply("NAME like UPPER(CONCAT('%', {0}, '%')) OR NAME LIKE LOWER(CONCAT('%', {0}, '%'))", key)
                .orderByAsc(TagDO::getName);
        return tagMapper.selectList(wrapper);
    }

    /**
     * 查询所有标签
     * @return 标签数据对象列表
     */
    @Override
    public List<TagDO> selectAll() {
        return tagMapper.selectList(null);
    }

    /**
     * 新增标签
     * @param tagDO 标签数据对象
     * @return 影响行数
     */
    @Override
    public int insert(TagDO tagDO) {
        return tagMapper.insert(tagDO);
    }

    /**
     * 查询标签总数（未删除状态）
     * @return 标签总数
     */
    @Override
    public Long selectTotalCount() {
        QueryWrapper<TagDO> wrapper = new QueryWrapper<>();
        wrapper.select("1").lambda().eq(TagDO::getIsDeleted, 0);
        return tagMapper.selectCount(wrapper);
    }
}
