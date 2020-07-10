/**
 * @filename:OrderMasterDao 2020年04月08日
 * @project up-mall板根商城  V1.0
 * Copyright(c) 2020 binggleWang Co. Ltd.
 * All right reserved.
 */
package com.zsl.upmall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zsl.upmall.entity.Tracking;
import com.zsl.upmall.vo.in.SkuAddStockVo;
import com.zsl.upmall.vo.in.SkuDetailVo;
import com.zsl.upmall.vo.out.BuyLimitVo;
import com.zsl.upmall.vo.out.OrderListVo;
import com.zsl.upmall.vo.out.SubpackageVo;
import com.zsl.upmall.vo.out.TrackingVo;
import org.apache.ibatis.annotations.Mapper;
import com.zsl.upmall.entity.OrderMaster;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description:订单(数据访问层)
 * @version: V1.0
 * @author: binggleWang
 */
@Mapper
public interface OrderMasterDao extends BaseMapper<OrderMaster> {
    IPage<OrderListVo> getOrderListByStatus(IPage<OrderListVo> page, @Param("orderStatus") Integer orderStatus, @Param("userId") Integer userId);

    /**
     * 根据skuId 获取sku详情
     *
     * @param skuId
     * @return
     */
    SkuDetailVo getSkuDetail(@Param("skuId") Integer skuId);


    /**
     * 扣减库存
     *
     * @param list
     * @param action
     * @return
     */
    int addAndSubSkuStock(@Param("list") List<SkuAddStockVo> list, @Param("action") boolean action, @Param("isSaleVolume") boolean isSaleVolume, @Param("isStock") boolean isStock);

    /**
     * 获取sku价格
     *
     * @param userId
     * @param skuId
     * @return
     */
    BigDecimal getSkuPriceByUserLevel(@Param("userId") Integer userId, @Param("skuId") Integer skuId);

    List<BuyLimitVo> isBuyLimit(@Param("memberId") Integer memberId,@Param("spuList") List<Integer> spuList);

    /**
     * 获取订单商品总数量
     *
     * @param orderId
     * @return
     */
    int getTotalProductCount(@Param("orderId") Integer orderId);

    /**
     * 获取订单 物流信息
     *
     * @param orderNo
     * @return
     */
    List<TrackingVo> getOrderTracking(String orderNo);

    /**
     * 分包裹物流信息
     *
     * @param orderNo
     * @return
     */
    List<SubpackageVo> getOrderSubpackageTracking(String orderNo);

    /**
     * 订单物流公司信息
     *
     * @param trackingSn
     * @return
     */
    Tracking orderTracking(String trackingSn);

    /**
     * 新增代理商订单信息
     *
     * @param orderMaster
     */
    void insertOrderMasterAgent(OrderMaster orderMaster);
}
