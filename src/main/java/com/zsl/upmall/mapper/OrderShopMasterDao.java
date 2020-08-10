/**
 * @filename:TrackingDao 2020年04月08日
 * @project up-mall板根商城  V1.0
 * Copyright(c) 2020 binggleWang Co. Ltd. 
 * All right reserved. 
 */
package com.zsl.upmall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsl.upmall.entity.OrderShopMaster;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**   
 * @Description:TODO(用户和代理商数据访问层)
 *
 * @version: V1.0
 * @author: binggleWang
 * 
 */
@Mapper
public interface OrderShopMasterDao extends BaseMapper<OrderShopMaster> {

    /**
     * 获取供应商订单主键id
     * @param orderId
     * @param shopId
     * @return
     */
    Long getOrderShopMasterId(@Param("orderId") Long orderId, @Param("shopId") Integer shopId);

    /**
     * 获取供应商订单主键集合
     * @param orderId
     * @return
     */
    List<Long> listOrderShopMasterId(Long orderId);
	
}
