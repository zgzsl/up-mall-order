/**
 * @filename:OrderDetailServiceImpl 2020年04月08日
 * @project up-mall板根商城  V1.0
 * Copyright(c) 2018 binggleWang Co. Ltd. 
 * All right reserved. 
 */
package com.zsl.upmall.service.impl;

import com.zsl.upmall.entity.OrderDetail;
import com.zsl.upmall.mapper.OrderDetailDao;
import com.zsl.upmall.service.OrderDetailService;
import com.zsl.upmall.vo.out.GoodsVo;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.List;

/**   
 * @Description:(服务实现)
 *
 * @version: V1.0
 * @author: binggleWang
 * 
 */
@Service
public class OrderDetailServiceImpl  extends ServiceImpl<OrderDetailDao, OrderDetail> implements OrderDetailService  {

   @Resource
   private OrderDetailDao orderDetailDao;
    /**
     * 订单商品名称
     * @param list
     * @return
     */
    @Override
    public List<GoodsVo> orderGoodsList(String[] list) {
        return orderDetailDao.orderGoodsList(list);
    }


}