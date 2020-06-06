package com.zsl.upmall.vo.in;

import lombok.Data;

@Data
public class AddressResultVo {
    private String code;
    private String message;
    private AddressInfo data;
}
