package com.zsl.upmall.vo;


/**
 * @ClassName GroupOrderStatusEnum
 * @Description 拼团订单状态
 * @Author binggleW
 * @Date 2020-05-14 10:36
 * @Version 1.0
 **/
public enum GroupOrderStatusEnum {
    HAVING(0,"组团中"),SUCCESS(1,"拼团成功"),FAILED(2,"组团失败");

    private Integer code;
    private String desc;

    GroupOrderStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
