/**
 * @filename:GrouponOrder 2020年04月08日
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
 * @Description:TODO(拼团主订单表实体类)
 * 
 * @version: V1.0
 * @author: binggleWang
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class GrouponOrder extends Model<GrouponOrder> {

	private static final long serialVersionUID = 1589254278864L;
	
	@TableId(value = "id", type = IdType.AUTO)
	 /** 自增主键 */
	private Integer id;
    
	 /** 拼团订单号 */
	private String grouponOrderNo;
    
	 /** 拼团活动主键ID */
	private Integer grouponActivitiesId;
    
	 /** 团长ID */
	private Integer userMemberId;
    
	 /** 组团状态（0：组团中；1：组团成功；2：组团失败） */
	private Integer grouponOrderStatus;
    
	 /** 拼团唯一凭证码 */
	private String grouponCode;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	 /** 开团时间 */
	private Date createTime;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	 /** 结束时间 */
	private Date endTime;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	/** 结算时间 */
	private Date settlementTime;

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
