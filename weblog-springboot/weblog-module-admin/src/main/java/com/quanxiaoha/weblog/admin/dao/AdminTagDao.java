package com.quanxiaoha.weblog.admin.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quanxiaoha.weblog.common.domain.dos.TagDO;

import java.util.Date;
import java.util.List;

/**
 * 标签数据访问接口
 * 处理标签的查询、新增和统计操作
 */
public interface AdminTagDao {
    /**
     * 分页查询标签列表（带筛选条件）
     * @param current 当前页码
     * @param size 每页条数
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param tagName 标签名称关键词
     * @return 标签分页数据对象
     */
    Page<TagDO> queryTagPageList(Long current, Long size, Date startDate, Date endDate, String tagName);

    /**
     * 根据关键词搜索标签
     * @param key 搜索关键词
     * @return 标签数据对象列表
     */
    List<TagDO> searchTags(String key);

    /**
     * 查询所有标签
     * @return 标签数据对象列表
     */
    List<TagDO> selectAll();

    /**
     * 新增标签
     * @param tagDO 标签数据对象
     * @return 影响行数
     */
    int insert(TagDO tagDO);

    /**
     * 查询标签总数
     * @return 标签总数
     */
    Long selectTotalCount();
}
