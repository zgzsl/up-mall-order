package com.zsl.upmall.vo.in;

import lombok.Data;

@Data
public class PayNotifyVo {
    private String result;
    private String notify_url;
    private String transaction_id;
    private String out_trade_no;
    private String payWay;
}
