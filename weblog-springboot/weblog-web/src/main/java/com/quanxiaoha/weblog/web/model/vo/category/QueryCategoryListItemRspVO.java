package com.quanxiaoha.weblog.web.model.vo.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryCategoryListItemRspVO {
    private Long id;
    private String name;
}
