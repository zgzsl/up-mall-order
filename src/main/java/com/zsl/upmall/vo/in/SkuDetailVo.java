package com.zsl.upmall.vo.in;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SkuDetailVo {
    private Integer skuId;
    private Integer spuId;
    private String skuName;
    private String spuName;
    private Integer salesVolume;
    private Integer stock;
    private Boolean status;
    private String skuImage;
    private BigDecimal skuPrice;
    private String spec;
    private Integer productCount;
    private String desc;
}
