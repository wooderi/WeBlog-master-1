package com.quanxiaoha.weblog.common.domain.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 访客记录数据对象
 * 映射数据库表：t_visitor_record
 */
@Data
@Builder
@TableName("t_visitor_record")
@AllArgsConstructor
@NoArgsConstructor
public class VisitorRecordDO {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 访客标识
     */
    private String visitor;
    
    /**
     * IP地址
     */
    private String ipAddress;
    
    /**
     * IP归属地
     */
    private String ipRegion;
    
    /**
     * 访问时间
     */
    private Date visitTime;
    
    /**
     * 是否通知（0-未通知，1-已通知）
     */
    private Integer isNotify;
}

