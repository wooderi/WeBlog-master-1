package com.quanxiaoha.weblog.admin.dao;

import com.quanxiaoha.weblog.common.domain.dos.StatisticsArticlePVDO;

import java.util.Date;
import java.util.List;

/**
 * 文章PV统计数据访问接口
 * 处理文章访问量(PV)的统计和查询操作
 */
public interface AdminStatisticsArticlePVDao {
    /**
     * 增加指定日期的PV统计
     * @param currDate 当前日期
     */
    void pvIncrease(Date currDate);

    /**
     * 查询最近一周的PV统计记录
     * @return 最近7天的PV统计数据对象列表
     */
    List<StatisticsArticlePVDO> selectLatestWeekRecords();

    /**
     * 查询所有PV统计数据
     * @return 所有PV统计数据对象列表
     */
    List<StatisticsArticlePVDO> selectAllPVCount();
}
