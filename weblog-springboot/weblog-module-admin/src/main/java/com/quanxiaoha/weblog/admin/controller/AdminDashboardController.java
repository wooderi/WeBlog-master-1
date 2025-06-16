package com.quanxiaoha.weblog.admin.controller;

import com.quanxiaoha.weblog.admin.model.vo.blogsetting.UpdateBlogSettingReqVO;
import com.quanxiaoha.weblog.admin.service.AdminBlogSettingService;
import com.quanxiaoha.weblog.admin.service.AdminDashboardService;
import com.quanxiaoha.weblog.common.Response;
import com.quanxiaoha.weblog.common.aspect.ApiOperationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 仪表盘控制器
 * 提供后台仪表盘数据统计功能，包括文章统计、发布趋势和PV访问量统计
 */
@RestController
@RequestMapping("/admin/dashboard")
public class AdminDashboardController {

    @Autowired
    private AdminDashboardService dashboardService;

    /**
     * 获取文章统计信息
     * @return 包含文章总数、分类总数、标签总数和总PV的统计数据
     */
    @PostMapping("/article/statistics")
    @ApiOperationLog(description = "获取后台仪表盘文章统计信息")
    public Response queryDashboardArticleStatisticsInfo() {
        return dashboardService.queryDashboardArticleStatisticsInfo();
    }

    /**
     * 获取文章发布趋势统计
     * @return 按日期分组的文章发布数量统计数据，用于图表展示
     */
    @PostMapping("/publishArticle/statistics")
    @ApiOperationLog(description = "获取后台仪表盘文章发布 echat 统计信息")
    public Response queryDashboardPublishArticleStatisticsInfo() {
        return dashboardService.queryDashboardPublishArticleStatisticsInfo();
    }

    /**
     * 获取PV访问量统计
     * @return 近7天的PV访问量统计数据，用于图表展示
     */
    @PostMapping("/pv/statistics")
    @ApiOperationLog(description = "获取后台仪表盘 PV echat 统计信息")
    public Response queryDashboardPVStatisticsInfo() {
        return dashboardService.queryDashboardPVStatisticsInfo();
    }

}
