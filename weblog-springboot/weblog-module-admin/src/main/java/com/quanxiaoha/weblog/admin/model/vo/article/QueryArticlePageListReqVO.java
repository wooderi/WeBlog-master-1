package com.quanxiaoha.weblog.admin.model.vo.article;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 文章分页查询请求参数VO
 * 包含页码、每页大小、日期范围和标题搜索关键词等筛选条件
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryArticlePageListReqVO {
    private Long current = 1L;
    private Long size = 10L;
    private Date startDate;
    private Date endDate;
    private String searchTitle;
}
