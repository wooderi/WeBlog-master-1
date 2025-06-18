package com.quanxiaoha.weblog.common.constant;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

/**
 * @description: 全局静态变量
 **/
public interface Constants {

    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    DateFormat MONTH_FORMAT = new SimpleDateFormat("yyyy-MM");
}
