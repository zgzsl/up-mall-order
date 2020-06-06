package com.zsl.upmall.vo.in;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName SkuGroupPriceResult
 * @Description TODO
 * @Author binggleW
 * @Date 2020-05-16 9:45
 * @Version 1.0
 **/
@Data
public class SkuGroupPriceResult {
    private Integer code;
    private String desc;
    private BigDecimal data;
}
