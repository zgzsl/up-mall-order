package com.zsl.upmall.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.security.cert.CertificateExpiredException;

/**
 * @ClassName MiniNoticeVo
 * @Description 通知粉妆妆泪
 * @Author binggleW
 * @Date 2020-05-16 14:15
 * @Version 1.0
 **/
@Data
public class MiniNoticeVo {
    private Integer activityMode;
    private Integer orderId;
    private String orderSn;
    private BigDecimal totalFee;
    private Integer goodsCount;
    private String goodsName;
    private String goodsSpc;
    private BigDecimal goodsPrice;
    private String openId;
}
