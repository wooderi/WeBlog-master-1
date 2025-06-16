package com.quanxiaoha.weblog.admin.service;


import com.quanxiaoha.weblog.common.Response;

/**
 * 仪表盘统计服务接口
 * 负责博客数据统计信息的查询
 */
public interface AdminDashboardService {

    /**
     * 查询文章统计信息
     * @return 包含文章总数、草稿数等统计数据的响应
     */
    Response queryDashboardArticleStatisticsInfo();

    /**
     * 查询文章发布统计信息
     * @return 包含按时间维度的文章发布量统计数据的响应
     */
    Response queryDashboardPublishArticleStatisticsInfo();

    /**
     * 查询页面访问量(PV)统计信息
     * @return 包含网站PV数据统计的响应
     */
    Response queryDashboardPVStatisticsInfo();
}
