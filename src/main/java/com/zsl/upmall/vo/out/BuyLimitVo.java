package com.zsl.upmall.vo.out;

import lombok.Data;

/**
 * @ClassName BuyLimitVo
 * @Description TODO
 * @Author binggleW
 * @Date 2020-06-30 16:36
 * @Version 1.0
 **/
@Data
public class BuyLimitVo {
    private Integer spuId;
    private Integer buyLimitCount;
    private Integer buyCount;
    private Integer limits;
}
