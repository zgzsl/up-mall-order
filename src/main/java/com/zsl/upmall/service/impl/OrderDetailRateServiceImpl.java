/**
 * @filename:OrderDetailRateServiceImpl 2020年04月08日
 * @project up-mall板根商城  V1.0
 * Copyright(c) 2018 binggleWang Co. Ltd. 
 * All right reserved. 
 */
package com.zsl.upmall.service.impl;

import com.zsl.upmall.entity.OrderDetailRate;
import com.zsl.upmall.mapper.OrderDetailRateDao;
import com.zsl.upmall.service.OrderDetailRateService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**   
 * @Description:订单评价(服务实现)
 *
 * @version: V1.0
 * @author: binggleWang
 * 
 */
@Service
public class OrderDetailRateServiceImpl  extends ServiceImpl<OrderDetailRateDao, OrderDetailRate> implements OrderDetailRateService  {
}