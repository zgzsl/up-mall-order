/**
 * @filename:SkuGrouponPrice 2020年04月08日
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
public class SkuGrouponPrice extends Model<SkuGrouponPrice> {

	private static final long serialVersionUID = 1589254281460L;
	
	@TableId(value = "id", type = IdType.AUTO)
	 /**  */
	private Integer id;
    
	 /**  */
	private Integer skuId;
    
	 /** 团购价 */
	private BigDecimal grouponPrice;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
