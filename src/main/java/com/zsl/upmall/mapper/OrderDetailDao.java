/**
 * @filename:OrderDetailDao 2020年04月08日
 * @project up-mall板根商城  V1.0
 * Copyright(c) 2020 binggleWang Co. Ltd.
 * All right reserved.
 */
package com.zsl.upmall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsl.upmall.vo.out.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import com.zsl.upmall.entity.OrderDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description:订单详情(数据访问层)
 * @version: V1.0
 * @author: binggleWang
 */
@Mapper
public interface OrderDetailDao extends BaseMapper<OrderDetail> {
    /**
     * 订单商品名称
     *
     * @param detailList
     * @return
     */
    List<GoodsVo> orderGoodsList(@Param("detailList") String[] detailList);

    /**
     * 代理商订单详情
     *
     * @param orderDetail
     */
    void insertOrderDetailAgent(OrderDetail orderDetail);
}
