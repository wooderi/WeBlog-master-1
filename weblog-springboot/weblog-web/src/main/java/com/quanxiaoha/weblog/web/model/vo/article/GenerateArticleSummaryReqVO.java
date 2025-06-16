package com.quanxiaoha.weblog.web.model.vo.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 生成文章摘要请求 VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GenerateArticleSummaryReqVO {
    
    @NotNull(message = "文章ID不能为空")
    private Long articleId;
} 