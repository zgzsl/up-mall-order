/**
 * @filename:OrderDetail 2020年04月08日
 * @project up-mall板根商城  V1.0
 * Copyright(c) 2020 binggleWang Co. Ltd. 
 * All right reserved. 
 */
package com.zsl.upmall.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description:订单详情
 *
 * @version: V1.0
 * @author: binggleWang
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderDetail extends Model<OrderDetail> {

	private static final long serialVersionUID = 1586829696433L;

	@TableId(value = "id", type = IdType.AUTO)
	/** id主键 */
	private Long id;

	/** 订单号 */
	private Long orderId;

	/** sku_id */
	private Integer skuId;

	/** 商品数量 */
	private Integer goodsCount;

	/** 实际数量（除开退款、售后等） */
	private Integer actualCount;

	/** 购买时单价 */
	private BigDecimal goodsPrice;

	/** 商品总运费 */
	private BigDecimal goodsCarriage;

	/** 商品总额 */
	private BigDecimal goodsAmount;

	/** 实际结算 */
	private BigDecimal practicalClearing;

	/** 结算信息 */
	private String clearingInfo;

	/** 商品名称 */
	private String goodsName;

	/** 商品图片 */
	private String goodsImg;

	/** 商品规格 */
	private String goodsSpec;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
