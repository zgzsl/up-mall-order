/**
 * @filename:OrderDetailService 2020年04月08日
 * @project up-mall板根商城  V1.0
 * Copyright(c) 2020 binggleWang Co. Ltd. 
 * All right reserved. 
 */
package com.zsl.upmall.service;

import com.zsl.upmall.entity.OrderDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zsl.upmall.vo.out.GoodsVo;

import java.util.List;

/**
 * @Description:(服务层)
 * @version: V1.0
 * @author: binggleWang
 * 
 */
public interface OrderDetailService extends IService<OrderDetail> {
    /**
     * 订单商品名称
     * @param list
     * @return
     */
    List<GoodsVo> orderGoodsList(String[] list);
}