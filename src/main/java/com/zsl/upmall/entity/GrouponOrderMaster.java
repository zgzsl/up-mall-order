/**
 * @filename:GrouponOrderMaster 2020年04月08日
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
import java.util.Date;

/**
 * @Description:TODO(实体类)
 *
 * @version: V1.0
 * @author: binggleWang
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class GrouponOrderMaster extends Model<GrouponOrderMaster> {

	private static final long serialVersionUID = 1589423004111L;

	@TableId(value = "id", type = IdType.AUTO)
	/** id主键 */
	private Integer id;

	/** 开团订单id */
	private Integer grouponOrderId;

	/** 订单id */
	private Integer orderId;

	/**  */
	private Integer memberId;

	/** 参团份额凭证（抽奖图若购买多个，则有多个凭证，此处需分割数据） */
	private String voucher;

	/** 中奖凭证 */
	private String winVoucher;

	/** 拼团结果 */
	private String grouponResult;

	/** 返还奖励金 */
	private BigDecimal backPrize;

	/** 拼团状态（0拼团中，1拼团成功，2拼团失败） */
	private Integer grouponStatus;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	/** 参团时间 */
	private Date createTime;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
