package com.zsl.upmall.util;

import lombok.Data;

@Data
public class CountUtil {
    private Integer num;

    public void add(){
        num++;
    }
}
