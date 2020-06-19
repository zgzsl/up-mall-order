package com.zsl.upmall.vo.out;

import lombok.Data;

import java.util.List;

@Data
public class SubpackageVo {

    private String trackingCompanyName;

    private String trackingNumber;

    private String subpackage;

    private List<GoodsVo> goodsList;

}
