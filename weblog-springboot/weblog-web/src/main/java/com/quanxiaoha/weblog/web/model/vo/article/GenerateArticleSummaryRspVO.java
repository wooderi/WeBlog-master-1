package com.quanxiaoha.weblog.web.model.vo.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 生成文章摘要响应 VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GenerateArticleSummaryRspVO {
    
    private String summary;
} 