package com.quanxiaoha.weblog.admin.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quanxiaoha.weblog.admin.dao.AdminArticleDao;
import com.quanxiaoha.weblog.common.domain.mapper.ArticleMapper;
import com.quanxiaoha.weblog.common.domain.dos.ArticleCountDO;
import com.quanxiaoha.weblog.common.domain.dos.ArticleDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;


/**
 * 文章数据访问实现类
 * 实现文章基本信息的新增、查询、更新、删除等数据库操作
 */
@Service
@Slf4j
public class AdminArticleDaoImpl implements AdminArticleDao {
    @Autowired
    private ArticleMapper articleMapper;

    /**
     * 新增文章基本信息
     * @param articleDO 文章数据对象
     * @return 影响行数
     */
    @Override
    public int insertArticle(ArticleDO articleDO) {
        return articleMapper.insert(articleDO);
    }

    /**
     * 根据文章ID查询文章信息
     * @param articleId 文章ID
     * @return 文章数据对象（未删除状态）
     */
    @Override
    public ArticleDO queryByArticleId(Long articleId) {
        QueryWrapper<ArticleDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ArticleDO::getId, articleId).eq(ArticleDO::getIsDeleted, 0);
        return articleMapper.selectOne(wrapper);
    }

    /**
     * 分页查询文章列表（带筛选条件）
     * @param current 当前页码
     * @param size 每页条数
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param searchTitle 标题搜索关键词
     * @return 文章分页数据对象
     */
    @Override
    public Page<ArticleDO> queryArticlePageList(Long current, Long size, Date startDate, Date endDate, String searchTitle) {
        Page<ArticleDO> page = new Page<>(current, size);
        QueryWrapper<ArticleDO> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .like(Objects.nonNull(searchTitle), ArticleDO::getTitle, searchTitle)
                .ge(Objects.nonNull(startDate), ArticleDO::getCreateTime, startDate)
                .le(Objects.nonNull(endDate), ArticleDO::getCreateTime, endDate)
                .orderByDesc(ArticleDO::getCreateTime);
        return articleMapper.selectPage(page, wrapper);
    }

    /**
     * 根据ID删除文章
     * @param articleId 文章ID
     * @return 影响行数
     */
    @Override
    public int deleteById(Long articleId) {
        return articleMapper.deleteById(articleId);
    }

    /**
     * 根据ID更新文章信息
     * @param articleDO 文章数据对象（包含更新信息和ID）
     * @return 影响行数
     */
    @Override
    public int updateById(ArticleDO articleDO) {
        return articleMapper.updateById(articleDO);
    }

    /**
     * 查询文章总数（未删除状态）
     * @return 文章总数
     */
    @Override
    public Long selectTotalCount() {
        QueryWrapper<ArticleDO> wrapper = new QueryWrapper<>();
        wrapper.select("1").lambda().eq(ArticleDO::getIsDeleted, 0);
        return articleMapper.selectCount(wrapper);
    }

    /**
     * 查询指定日期范围内的文章发布数量统计
     * @param startDate 开始日期（字符串格式）
     * @param endDate 结束日期（字符串格式）
     * @return 按日期分组的文章数量统计列表
     */
    @Override
    public List<ArticleCountDO> selectArticleCount(String startDate, String endDate) {
        return articleMapper.selectArticleCount(startDate, endDate);
    }

    /**
     * 增加文章阅读量（+1）
     * @param articleId 文章ID
     * @return 影响行数
     */
    @Override
    public int readNumIncrease(Long articleId) {
        UpdateWrapper<ArticleDO> wrapper = new UpdateWrapper<>();
        wrapper.lambda().setSql("read_num = read_num + 1").eq(ArticleDO::getId, articleId);
        return articleMapper.update(null, wrapper);
    }
}
