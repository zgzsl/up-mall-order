/**
 * @filename:OrderMasterService 2020年04月08日
 * @project up-mall板根商城  V1.0
 * Copyright(c) 2020 binggleWang Co. Ltd. 
 * All right reserved. 
 */
package com.zsl.upmall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zsl.upmall.entity.OrderMaster;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zsl.upmall.vo.in.SkuAddStockVo;
import com.zsl.upmall.vo.in.SkuDetailVo;
import com.zsl.upmall.vo.out.OrderListVo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description:订单详情（服务层)
 * @version: V1.0
 * @author: binggleWang
 * 
 */
public interface OrderMasterService extends IService<OrderMaster> {
    IPage<OrderListVo> getOrderListByStatus(IPage<OrderListVo> page,Integer orderStatus, Integer userId);

    /**
     * 根据skuId 获取sku详情
     * @param skuId
     * @return
     */
    SkuDetailVo getSkuDetail(Integer skuId);

    /**
     * 扣减库存
     * @param list
     * @param action
     * @return
     */
    int addAndSubSkuStock(List<SkuAddStockVo> list, boolean action, boolean isSaleVolume, boolean isStock);

    /**
     * 获取sku价格
     * @param userId
     * @param skuId
     * @return
     */
    BigDecimal getSkuPriceByUserLevel(Integer userId,Integer skuId);

    /**
     * 获取订单商品总数量
     * @param orderId
     * @return
     */
    int getTotalProductCount(Integer orderId);
}