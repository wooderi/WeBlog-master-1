package com.quanxiaoha.weblog.web.model.vo.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "搜索文章分页请求VO")
public class QuerySearchArticlePageListReqVO {
    @ApiModelProperty(value = "当前页码")
    @NotNull(message = "页码不能为空")
    private Long current;

    @ApiModelProperty(value = "每页显示数量")
    @NotNull(message = "每页显示数量不能为空")
    private Long size;

    @ApiModelProperty(value = "搜索关键词")
    @NotBlank(message = "搜索关键词不能为空")
    private String keyword;
}