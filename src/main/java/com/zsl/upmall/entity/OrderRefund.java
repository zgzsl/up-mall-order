/**
 * @filename:OrderRefund 2020年04月08日
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
 * @Description:TODO(订单退款表实体类)
 * 
 * @version: V1.0
 * @author: binggleWang
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderRefund extends Model<OrderRefund> {

	private static final long serialVersionUID = 1589533872378L;
	
	@TableId(value = "id", type = IdType.AUTO)
	 /** 主键 */
	private Integer id;
    
	 /** 订单id */
	private Integer orderId;
    
	 /** 微信订单号 */
	private String transactionId;
    
	 /** 商户订单号 */
	private String outTradeNo;
    
	 /** 商户退款单号 */
	private String outRefundNo;
    
	 /** 订单总金额，单位为分，只能为整数 */
	private Integer totalFee;
    
	 /** 申请退款金额，单位为分，只能为整数 */
	private Integer refundFee;
    
	 /** 退款原因 */
	private String refundDesc;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	 /** 创建时间 */
	private Date createTime;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	 /** 退款时间 */
	private Date refundTime;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
