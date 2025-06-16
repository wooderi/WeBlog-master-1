package com.quanxiaoha.weblog.admin.model.vo.article;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 查询文章详情请求参数VO
 * 根据文章ID获取文章详细信息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryArticleDetailReqVO {
    private Long articleId;
}
