package com.zsl.upmall.vo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @ClassName BalacneRebateVo
 * @Description 余额发放封装类
 * @Author binggleW
 * @Date 2020-05-15 14:49
 * @Version 1.0
 **/
@Data
@Builder
@Accessors(chain = true)
public class BalacneRebateVo {
    private Integer userId;
    private BigDecimal balance;
}
