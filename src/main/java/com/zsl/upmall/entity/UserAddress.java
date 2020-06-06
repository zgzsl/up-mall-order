/**
 * @filename:UserAddress 2020年04月08日
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
public class UserAddress extends Model<UserAddress> {

	private static final long serialVersionUID = 1587090835664L;
	
	@TableId(value = "id", type = IdType.AUTO)
	 /** id主键 */
	private Integer id;
    
	 /** 收货人名称 */
	private String name;
    
	 /** 用户表的用户ID */
	private Integer userId;
    
	 /** 区域表主键id */
	private Integer administrativeRegionId;
    
	 /** 详细收货地址 */
	private String addressDetail;
    
	 /** 手机号码 */
	private String tel;
    
	 /** 是否默认地址 */
	private Integer isDefault;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	 /** 创建时间 */
	private Date addTime;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	 /** 更新时间 */
	private Date updateTime;
    
	 /** 逻辑删除（0:没有删除，1：删除） */
	private Integer isDelete;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
