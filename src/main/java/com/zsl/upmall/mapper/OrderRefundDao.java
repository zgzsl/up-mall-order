/**
 * @filename:OrderRefundDao 2020年04月08日
 * @project up-mall板根商城  V1.0
 * Copyright(c) 2020 binggleWang Co. Ltd. 
 * All right reserved. 
 */
package com.zsl.upmall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.zsl.upmall.entity.OrderRefund;

/**   
 * @Description:TODO(订单退款表数据访问层)
 *
 * @version: V1.0
 * @author: binggleWang
 * 
 */
@Mapper
public interface OrderRefundDao extends BaseMapper<OrderRefund> {
	
}
