package com.quanxiaoha.weblog.web.model.vo.archive;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryArchivePageListReqVO {
    private Long current = 1L;
    private Long size = 10L;
}
