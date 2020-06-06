package com.zsl.upmall.vo;

import lombok.Data;

/**
 * @ClassName RefundNotifyVo
 * @Description 退款回调
 * @Author binggleW
 * @Date 2020-05-15 19:11
 * @Version 1.0
 **/
@Data
public class RefundNotifyVo {
    private String out_refund_no;
    private String out_trade_no;
}
