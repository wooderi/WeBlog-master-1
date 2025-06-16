package com.quanxiaoha.weblog.web.model.vo.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * ��������ժҪ���� VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GenerateArticleSummaryReqVO {
    
    @NotNull(message = "����ID����Ϊ��")
    private Long articleId;
} 