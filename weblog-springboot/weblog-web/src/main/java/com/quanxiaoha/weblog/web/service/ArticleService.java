package com.quanxiaoha.weblog.web.service;

import com.quanxiaoha.weblog.common.PageResponse;
import com.quanxiaoha.weblog.common.Response;
import com.quanxiaoha.weblog.web.model.vo.article.GenerateArticleSummaryReqVO;
import com.quanxiaoha.weblog.web.model.vo.article.QueryArticleDetailReqVO;
import com.quanxiaoha.weblog.web.model.vo.article.QueryCategoryArticlePageListReqVO;
import com.quanxiaoha.weblog.web.model.vo.article.QueryIndexArticlePageListReqVO;
import com.quanxiaoha.weblog.web.model.vo.article.QuerySearchArticlePageListReqVO;
import com.quanxiaoha.weblog.web.model.vo.article.QueryTagArticlePageListReqVO;

public interface ArticleService {

    PageResponse queryIndexArticlePageList(QueryIndexArticlePageListReqVO queryIndexArticlePageListReqVO);

    PageResponse queryCategoryArticlePageList(QueryCategoryArticlePageListReqVO queryCategoryArticlePageListReqVO);

    Response queryArticleDetail(QueryArticleDetailReqVO queryArticleDetailReqVO);

    PageResponse queryTagArticlePageList(QueryTagArticlePageListReqVO queryTagArticlePageListReqVO);

    Response generateArticleSummary(GenerateArticleSummaryReqVO generateArticleSummaryReqVO);

    PageResponse querySearchArticlePageList(QuerySearchArticlePageListReqVO querySearchArticlePageListReqVO);
}
