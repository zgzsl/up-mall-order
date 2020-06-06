package com.zsl.upmall.vo.in;

import lombok.Data;

@Data
public class SkuResult {
    private Integer code;
    private String desc;
    private SkuDetailVo data;
}
