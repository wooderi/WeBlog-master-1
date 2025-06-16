package com.quanxiaoha.weblog.web.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Deepseek 摘要生成响应 DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeepseekSummaryResponseDTO {
    
    private String id;
    private String object;
    private Long created;
    private String model;
    private List<Choice> choices;
    private Usage usage;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Choice {
        private Integer index;
        private Message message;
        private String finishReason;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Message {
        private String role;
        private String content;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Usage {
        private Integer promptTokens;
        private Integer completionTokens;
        private Integer totalTokens;
    }
} 