/**
 * @filename:ShoppingCart 2020年04月08日
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
 * @Description:TODO(用户和代理商实体类)
 * 
 * @version: V1.0
 * @author: binggleWang
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ShoppingCart extends Model<ShoppingCart> {

	private static final long serialVersionUID = 1587366584941L;
	
	@TableId(value = "id", type = IdType.AUTO)
	 /** 自增ID */
	private Integer id;
    
	 /** sku_id */
	private Integer skuId;
    
	 /** 用户ID */
	private Integer userId;
    
	 /** 商品数量 */
	private Integer goodsNum;
    
	 /** 加入时的单价 */
	private BigDecimal addedPrice;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
