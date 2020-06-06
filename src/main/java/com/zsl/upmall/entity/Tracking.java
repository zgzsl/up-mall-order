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
public class Tracking extends Model<Tracking> {

	private static final long serialVersionUID = 1586845173324L;
	
	 @TableId(value = "tracking_id", type = IdType.AUTO)
	private Integer trackingId;
    
	private String trackingCompanyName;
    
	private String trackingCode;
    

	@Override
    protected Serializable pkVal() {
        return this.trackingId;
    }
}
