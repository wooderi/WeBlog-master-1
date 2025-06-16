package com.quanxiaoha.weblog.common.enums;

import com.quanxiaoha.weblog.common.exception.BaseExceptionInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description: 响应枚举
 **/
@Getter
@AllArgsConstructor
public enum EventEnum {

    // PV 加 1
    PV_INCREASE("PV INCREASE"),
    ;

    private String message;

}
