package com.quanxiaoha.weblog.web.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Deepseek ?????????? DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeepseekSummaryRequestDTO {
    
    private String model;
    private List<Map<String, String>> messages;
    private Boolean stream;
} 