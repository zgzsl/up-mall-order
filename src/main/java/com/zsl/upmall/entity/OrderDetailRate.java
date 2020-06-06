/**
 * @filename:OrderDetailRate 2020年04月08日
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
 * @Description:、订单评价
 * 
 * @version: V1.0
 * @author: binggleWang
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderDetailRate extends Model<OrderDetailRate> {

	private static final long serialVersionUID = 1586348736592L;
	
	@TableId(value = "id", type = IdType.AUTO)
	 /** id主键 */
	private Integer id;
    
	 /** 订单详情id */
	private Long orderDetailId;
    
	 /** 用户id */
	private Integer memberId;
    
	 /** 满意度(星级) */
	private Integer degreeOfSatisfaction;
    
	 /** 评价内容 */
	private String evaluationContent;
    
	 /** 评价图片 */
	private String evaluationImageUrl;
    
	 /** 是否匿名（0非匿名，1匿名） */
	private Integer anonymity;
    
	 /** 卖家回复 */
	private String sellerReply;
    
	 /** 追加评价 */
	private String additionalEvaluation;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
