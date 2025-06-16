package com.quanxiaoha.weblog.admin.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.quanxiaoha.weblog.admin.dao.AdminArticleContentDao;
import com.quanxiaoha.weblog.common.domain.mapper.ArticleContentMapper;
import com.quanxiaoha.weblog.common.domain.dos.ArticleContentDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 文章内容数据访问实现类
 * 实现文章内容的新增、查询、更新和删除等数据库操作
 */
@Service
@Slf4j
public class AdminArticleContentDaoImpl implements AdminArticleContentDao {
    @Autowired
    private ArticleContentMapper articleContentMapper;

    /**
     * 新增文章内容
     * @param articleContentDO 文章内容数据对象
     * @return 影响行数
     */
    @Override
    public int insertArticleContent(ArticleContentDO articleContentDO) {
        return articleContentMapper.insert(articleContentDO);
    }

    /**
     * 根据文章ID查询内容
     * @param articleId 文章ID
     * @return 文章内容数据对象
     */
    @Override
    public ArticleContentDO queryByArticleId(Long articleId) {
        QueryWrapper<ArticleContentDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ArticleContentDO::getArticleId, articleId);
        return articleContentMapper.selectOne(wrapper);
    }

    /**
     * 根据文章ID删除内容
     * @param articleId 文章ID
     * @return 影响行数
     */
    @Override
    public int deleteByArticleId(Long articleId) {
        QueryWrapper<ArticleContentDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ArticleContentDO::getArticleId, articleId);
        return articleContentMapper.delete(wrapper);
    }

    /**
     * 根据文章ID更新内容
     * @param articleContentDO 文章内容数据对象（包含更新内容和文章ID）
     * @return 影响行数
     */
    @Override
    public int updateByArticleId(ArticleContentDO articleContentDO) {
        UpdateWrapper<ArticleContentDO> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(ArticleContentDO::getArticleId, articleContentDO.getArticleId());
        return articleContentMapper.update(articleContentDO, wrapper);
    }
}
