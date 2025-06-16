package com.quanxiaoha.weblog.admin.model.vo.article;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 查询文章详情响应VO
 * 包含文章ID、标题、头图、内容、摘要、分类ID和标签ID列表等信息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryArticleDetailRspVO {
    private Long id;
    private String title;
    private String titleImage;
    private String content;
    private String description;
    private Long categoryId;
    private List<Long> tagIds;
}
