/**
 * @filename:TrackingServiceImpl 2020年04月08日
 * @project up-mall板根商城  V1.0
 * Copyright(c) 2018 binggleWang Co. Ltd. 
 * All right reserved. 
 */
package com.zsl.upmall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsl.upmall.entity.OrderShopMaster;
import com.zsl.upmall.mapper.OrderShopMasterDao;
import com.zsl.upmall.service.OrderShopMasterService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**   
 * @Description:TODO(用户和代理商服务实现)
 *
 * @version: V1.0
 * @author: binggleWang
 * 
 */
@Service
public class OrderShopMasterServiceImpl extends ServiceImpl<OrderShopMasterDao, OrderShopMaster> implements OrderShopMasterService {
    @Resource
    private OrderShopMasterDao orderShopMasterDao;

    @Override
    public Long getOrderShopMasterId(Long orderId, Integer shopId) {
        return orderShopMasterDao.getOrderShopMasterId(orderId, shopId);
    }

    @Override
    public List<Long> listOrderShopMasterId(Long orderId) {
        return orderShopMasterDao.listOrderShopMasterId(orderId);
    }
}