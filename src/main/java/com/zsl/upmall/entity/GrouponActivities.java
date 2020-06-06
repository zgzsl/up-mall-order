/**
 * @filename:GrouponActivities 2020年04月08日
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
 * @Description:TODO(拼单活动表实体类)
 * 
 * @version: V1.0
 * @author: binggleWang
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class GrouponActivities extends Model<GrouponActivities> {

	private static final long serialVersionUID = 1589254278655L;
	
	@TableId(value = "id", type = IdType.AUTO)
	 /** 自增主键 */
	private Integer id;
    
	 /** SPU */
	private Integer spuId;
    
	 /** 活动主题 */
	private String activityTitle;
    
	 /** 活动封面 */
	private String activityCover;
    
	 /** 活动描述 */
	private String activityDescribe;
    
	 /** 分享标题 */
	private String shareTitle;
    
	 /** 分享描述 */
	private String shareDesc;
    
	 /** 分享封面 */
	private String shareCover;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	 /** 活动开始时间 */
	private Date startTime;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	 /** 活动截止时间 */
	private Date endTime;
    
	 /** 成团有效时间（单位：小时） */
	private Integer expireHour;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	 /** 创建时间 */
	private Date createTime;
    
	 /** 权重（数值越小，优先级越高，大于0则标记主页显示） */
	private Integer weight;
    
	 /** 拼团方式（0：普通团；1：抽奖团；2：阶梯团） */
	private Integer mode;
    
	 /** 成团人数 */
	private Integer groupCount;
    
	 /** 有效结算人数（普通团该值与团人数相同） */
	private Integer prizeCount;
    
	 /** 拼团奖励金 */
	private BigDecimal bounty;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
