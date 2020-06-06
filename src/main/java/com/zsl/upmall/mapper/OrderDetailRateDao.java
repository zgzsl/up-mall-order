/**
 * @filename:OrderDetailRateDao 2020年04月08日
 * @project up-mall板根商城  V1.0
 * Copyright(c) 2020 binggleWang Co. Ltd. 
 * All right reserved. 
 */
package com.zsl.upmall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.zsl.upmall.entity.OrderDetailRate;

/**   
 * @Description:订单评价(数据访问层)
 *
 * @version: V1.0
 * @author: binggleWang
 * 
 */
@Mapper
public interface OrderDetailRateDao extends BaseMapper<OrderDetailRate> {
	
}
