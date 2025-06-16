package com.quanxiaoha.weblog.admin.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.quanxiaoha.weblog.admin.dao.AdminStatisticsArticlePVDao;
import com.quanxiaoha.weblog.common.domain.dos.StatisticsArticlePVDO;
import com.quanxiaoha.weblog.common.domain.mapper.StatisticsArticlePVMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 文章PV统计数据访问实现类
 * 处理文章访问量(PV)的统计和查询等数据库操作
 */
@Service
@Slf4j
public class AdminStatisticsArticlePVDaoImpl implements AdminStatisticsArticlePVDao {

    @Autowired
    private StatisticsArticlePVMapper statisticsArticlePVMapper;

    /**
     * 增加指定日期的PV统计
     * 如果当天记录不存在则创建，存在则更新计数+1
     * @param currDate 当前日期
     */
    @Override
    public void pvIncrease(Date currDate) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = dateFormat.format(currDate);

        QueryWrapper<StatisticsArticlePVDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().apply("pv_date = {0}", dateStr);
        Long count = statisticsArticlePVMapper.selectCount(queryWrapper);

        if (count > 0) {
            UpdateWrapper<StatisticsArticlePVDO> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().setSql("pv_count = pv_count + 1").apply("pv_date = {0}", dateStr);
            statisticsArticlePVMapper.update(null, updateWrapper);
        } else {
            StatisticsArticlePVDO statisticsArticlePVDO = StatisticsArticlePVDO.builder()
                    .pvCount(1L)
                    .pvDate(currDate)
                    .createTime(new Date())
                    .build();
            statisticsArticlePVMapper.insert(statisticsArticlePVDO);
        }
    }

    /**
     * 查询最近一周的PV统计记录
     * 按日期倒序排列，最多返回7条记录
     * @return 最近7天的PV统计数据对象列表
     */
    @Override
    public List<StatisticsArticlePVDO> selectLatestWeekRecords() {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = dateFormat.format(date);

        QueryWrapper<StatisticsArticlePVDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().apply("pv_date <= {0}", dateStr)
                .orderByDesc(StatisticsArticlePVDO::getPvDate)
                .last("limit 7");
        return statisticsArticlePVMapper.selectList(wrapper);
    }

    /**
     * 查询所有PV统计数据
     * @return 所有日期的PV统计数据对象列表
     */
    @Override
    public List<StatisticsArticlePVDO> selectAllPVCount() {
        return statisticsArticlePVMapper.selectList(null);
    }
}
