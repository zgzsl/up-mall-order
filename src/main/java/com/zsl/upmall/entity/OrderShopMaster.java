/**
 * @filename:Tracking 2020年04月08日
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
import java.util.Date;

/**   
 * @Description:TODO(用户和代理商实体类)
 * 
 * @version: V1.0
 * @author: binggleWang
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderShopMaster extends Model<OrderShopMaster> {

	private static final long serialVersionUID = 1586845173324L;
	
	 @TableId(value = "id", type = IdType.AUTO)
	private Long id;
    
	private Long orderMasterId;
    
	private Integer shopId;

	private String orderNo;

	private Integer currentState;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createTime;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
