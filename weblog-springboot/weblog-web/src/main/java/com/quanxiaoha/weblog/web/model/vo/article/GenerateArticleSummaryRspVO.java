package com.quanxiaoha.weblog.web.model.vo.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ��������ժҪ��Ӧ VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GenerateArticleSummaryRspVO {
    
    private String summary;
} 