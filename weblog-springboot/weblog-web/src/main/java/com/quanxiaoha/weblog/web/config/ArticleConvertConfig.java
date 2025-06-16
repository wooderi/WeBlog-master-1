package com.quanxiaoha.weblog.web.config;

import com.quanxiaoha.weblog.common.constant.Constants;
import com.quanxiaoha.weblog.common.domain.dos.ArticleDO;
import com.quanxiaoha.weblog.web.convert.ArticleConvert;
import com.quanxiaoha.weblog.web.model.vo.archive.QueryArchiveItemRspVO;
import com.quanxiaoha.weblog.web.model.vo.article.QueryIndexArticlePageItemRspVO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.Objects;

/**
 * 提供ArticleConvert bean的配置类
 */
@Configuration
public class ArticleConvertConfig {

    @Bean
    public ArticleConvert articleConvert() {
        return new ArticleConvert() {
            @Override
            public QueryIndexArticlePageItemRspVO convert(ArticleDO bean) {
                if (bean == null) {
                    return null;
                }
                
                QueryIndexArticlePageItemRspVO vo = new QueryIndexArticlePageItemRspVO();
                vo.setId(bean.getId());
                vo.setTitle(bean.getTitle());
                vo.setDescription(bean.getDescription());
                vo.setCreateTime(formatDate(bean.getCreateTime()));
                // titleImage可能也需要设置，取决于具体需求
                if (bean.getTitleImage() != null) {
                    vo.setTitleImage(bean.getTitleImage());
                }
                
                return vo;
            }
            
            @Override
            public QueryArchiveItemRspVO convert2Archive(ArticleDO bean) {
                if (bean == null) {
                    return null;
                }
                
                QueryArchiveItemRspVO vo = new QueryArchiveItemRspVO();
                vo.setId(bean.getId());
                vo.setTitle(bean.getTitle());
                vo.setCreateTime(formatDate(bean.getCreateTime()));
                vo.setCreateMonth(formatMonth(bean.getCreateTime()));
                if (bean.getTitleImage() != null) {
                    vo.setTitleImage(bean.getTitleImage());
                }
                
                return vo;
            }
            
            @Override
            public String formatDate(Date date) {
                if (Objects.isNull(date))
                    return null;
                return Constants.DATE_FORMAT.format(date);
            }
            
            @Override
            public String formatMonth(Date date) {
                if (Objects.isNull(date))
                    return null;
                return Constants.MONTH_FORMAT.format(date);
            }
        };
    }
} 