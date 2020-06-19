package com.zsl.upmall.vo.out;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class TrackingVo {

    private String trackingCompanyName;

    private String trackingNumber;

    private List<GoodsVo> goodsList;

}
