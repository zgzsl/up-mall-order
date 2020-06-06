package com.zsl.upmall.vo.out;

import lombok.Data;

@Data
public class UnifiedOrderVo {
   private Integer statusCode;
   private String statusMsg;
   private UnifiedData data;
}



@Data
class UnifiedData{
    private String appid;
    private String partnerid;
    private String prepayid;
    private String packageValue;
    private String noncestr;
    private String timestamp;
    private String signType;
    private String sign;
    private String payUrl;
}