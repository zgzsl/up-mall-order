package com.zsl.upmall.vo.in;

import com.zsl.upmall.validator.FlagValidator;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateOrderVo {
    /**
     * 地址id
     */
    @NotNull(message = "地址不能为空")
    private Integer addressId;

    /**
     * 分享人的分享码(有就传，没有随意，都可以)
     */
    private String shareId;

    /**
     * 微信openid
     */
    private String openid;

    /**
     * 套餐标识
     */
    private String comboLevel;


    /**
     * 订单号(用于区分下单，和去支付)
     */
    private String orderSn;

    /**
     * 购物车id(购物车结算传购物车1，不是购物车，传0)
     */
    @FlagValidator(value = {"0","1"},message = "只能为0或者1")
    private Integer cartId;

    /**
     * 购物车，结算的购物车id数组
     */
    private List<Integer> cartIdList;
    /**
     * 支付方式（1：支付宝，2：微信，3：余额支付）
     */
    @FlagValidator(value = {"1","2","3"},message = "只能为1,2,3")
    private Integer payWay;
    /**
     * 商家id
     */
    private Integer shopId;

    /**
     * 订单总金额
     */
    @NotNull(message = "总金额不能为空")
    private BigDecimal totalAmount;

    /**
     * 运费
     */
    @NotNull(message = "运费不能为空")
    private BigDecimal freight;

    /**
     * 商品id
     */
    private Integer productId;

    /**
     * 商品数量
     */
    private Integer productCount;

    /**
     * 拼团活动id (如果不是拼团，则传0，或者不传，如果是拼团，则传具体的拼团活动id)
     */
    private Integer grouponActivityId = 0;

    /**
     * 参团id（如果不是拼团，不传，如果是拼团（分两种情况：1，自己开团，2：参加别人的团））
     * 自己开团传 0
     * 参加别人的团 传具体的团的id
     */
    private Integer joinGroupId = -1;
}
