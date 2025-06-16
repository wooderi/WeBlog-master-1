package com.quanxiaoha.weblog.web.controller;

import com.quanxiaoha.weblog.common.PageResponse;
import com.quanxiaoha.weblog.common.Response;
import com.quanxiaoha.weblog.common.aspect.ApiOperationLog;
import com.quanxiaoha.weblog.web.model.vo.article.GenerateArticleSummaryReqVO;
import com.quanxiaoha.weblog.web.model.vo.article.QueryArticleDetailReqVO;
import com.quanxiaoha.weblog.web.model.vo.article.QuerySearchArticlePageListReqVO;
import com.quanxiaoha.weblog.web.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping("/detail")
    @ApiOperationLog(description = "获取文章详情信息")
    public Response queryArticleDetail(@RequestBody @Validated QueryArticleDetailReqVO queryArticleDetailReqVO) {
        return articleService.queryArticleDetail(queryArticleDetailReqVO);
    }

    @PostMapping("/{id}/summary")
    @ApiOperationLog(description = "生成文章摘要")
    public Response generateArticleSummary(@PathVariable("id") Long articleId) {
        GenerateArticleSummaryReqVO reqVO = GenerateArticleSummaryReqVO.builder()
                .articleId(articleId)
                .build();
        return articleService.generateArticleSummary(reqVO);
    }

    @PostMapping("/search")
    @ApiOperationLog(description = "搜索文章")
    public PageResponse searchArticles(
            @RequestBody @Validated QuerySearchArticlePageListReqVO querySearchArticlePageListReqVO) {
        return articleService.querySearchArticlePageList(querySearchArticlePageListReqVO);
    }
}
