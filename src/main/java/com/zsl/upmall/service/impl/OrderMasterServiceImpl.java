/**
 * @filename:OrderMasterServiceImpl 2020年04月08日
 * @project up-mall板根商城  V1.0
 * Copyright(c) 2018 binggleWang Co. Ltd.
 * All right reserved.
 */
package com.zsl.upmall.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zsl.upmall.entity.OrderMaster;
import com.zsl.upmall.entity.Tracking;
import com.zsl.upmall.mapper.OrderMasterDao;
import com.zsl.upmall.service.OrderMasterService;
import com.zsl.upmall.vo.in.SkuAddStockVo;
import com.zsl.upmall.vo.in.SkuDetailVo;
import com.zsl.upmall.vo.out.BuyLimitVo;
import com.zsl.upmall.vo.out.OrderListVo;
import com.zsl.upmall.vo.out.SubpackageVo;
import com.zsl.upmall.vo.out.TrackingVo;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description:订单(服务实现)
 *
 * @version: V1.0
 * @author: binggleWang
 *
 */
@Service
public class OrderMasterServiceImpl extends ServiceImpl<OrderMasterDao, OrderMaster> implements OrderMasterService {

    @Override
    public IPage<OrderListVo> getOrderListByStatus(IPage<OrderListVo> page, Integer orderStatus, Integer userId) {
        return this.baseMapper.getOrderListByStatus(page, orderStatus, userId);
    }

    @Override
    public IPage<OrderListVo> getOrderMasterListByStatus(IPage<OrderListVo> page, Integer orderStatus, Integer userId) {
        return this.baseMapper.getOrderMasterListByStatus(page,orderStatus,userId);
    }

    /**
     * 根据skuId 获取sku详情
     *
     * @param skuId
     * @return
     */
    @Override
    public SkuDetailVo getSkuDetail(Integer skuId) {
        return this.baseMapper.getSkuDetail(skuId);
    }

    /**
     * 扣减库存
     *
     * @param list
     * @param action
     * @return
     */
    @Override
    public int addAndSubSkuStock(List<SkuAddStockVo> list, boolean action, boolean isSaleVolume, boolean isStock) {
        return this.baseMapper.addAndSubSkuStock(list, action, isSaleVolume, isStock);
    }

    /**
     * 获取sku价格
     *
     * @param userId
     * @param skuId
     * @return
     */
    @Override
    public BigDecimal getSkuPriceByUserLevel(Integer userId, Integer skuId) {
        return this.baseMapper.getSkuPriceByUserLevel(userId, skuId);
    }

    /**
     * 获取订单商品总数量
     *
     * @param orderId
     * @return
     */
    @Override
    public int getTotalProductCount(Integer orderId) {
        return this.baseMapper.getTotalProductCount(orderId);
    }

    /**
     * 获取订单 物流信息
     * @param orderNo
     * @return
     */
    @Override
    public List<TrackingVo> getOrderTracking(String orderNo) {
        return baseMapper.getOrderTracking(orderNo);
    }

    /**
     * 分包裹物流信息
     * @param orderNo
     * @return
     */
    @Override
    public List<SubpackageVo> getOrderSubpackageTracking(String orderNo) {
        return baseMapper.getOrderSubpackageTracking(orderNo);
    }

    /**
     * 订单物流公司信息
     * @param trackingSn
     * @return
     */
    @Override
    public Tracking orderTracking(String trackingSn) {
        return baseMapper.orderTracking(trackingSn);
    }

    @Override
    public List<BuyLimitVo> isBuyLimit(Integer memberId, List<Integer> spuList) {
        return baseMapper.isBuyLimit(memberId,spuList);
    }

    @Override
    public int countOrderNum(Integer userId) {
        return baseMapper.countOrderNum(userId);
    }


}