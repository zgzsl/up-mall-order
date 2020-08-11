package com.zsl.upmall.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zsl.upmall.config.SystemConfig;
import com.zsl.upmall.entity.OrderDetail;
import com.zsl.upmall.entity.OrderMaster;
import com.zsl.upmall.entity.OrderShopMaster;
import com.zsl.upmall.service.OrderDetailService;
import com.zsl.upmall.service.OrderMasterService;
import com.zsl.upmall.service.OrderShopMasterService;
import com.zsl.upmall.service.RedisService;
import com.zsl.upmall.util.BeanUtil;
import com.zsl.upmall.vo.in.SkuAddStockVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public class OrderUnpaidTask extends Task {
    private final Logger logger = LoggerFactory.getLogger(OrderUnpaidTask.class);
    private long orderId = -1;

    public OrderUnpaidTask(long orderId, long delayInMilliseconds){
        super("OrderUnpaidTask-" + orderId, delayInMilliseconds);
        this.orderId = orderId;
    }

    public OrderUnpaidTask(long orderId){
        super("OrderUnpaidTask-" + orderId, SystemConfig.ORDER_UNPAID);
        this.orderId = orderId;
    }

    @Override
    public void run() {
        logger.info("系统开始处理延时任务---订单超时未付款---" + this.orderId);

        OrderMasterService orderService = BeanUtil.getBean(OrderMasterService.class);
        OrderShopMasterService shopMasterService = BeanUtil.getBean(OrderShopMasterService.class);
        OrderDetailService orderDetailService = BeanUtil.getBean(OrderDetailService.class);
        RedisService redisService = BeanUtil.getBean(RedisService.class);


        //判断订单是否存在
        OrderMaster order = orderService.getById(this.orderId);
        if(order == null){
            return;
        }

        //判断订单状态是否为待付款
        if(order.getOrderStatus() - SystemConfig.ORDER_STATUS_WAIT_PAY != 0){
            return ;
        }

        // 设置订单已取消状态
        OrderMaster upOrderCancel = new OrderMaster();
        upOrderCancel.setId(order.getId());
        upOrderCancel.setOrderStatus(SystemConfig.ORDER_STATUS_CANCLE );
        upOrderCancel.setCancelTime(new Date());
        if (!orderService.updateById(upOrderCancel)) {
            throw new RuntimeException("更新数据已失效");
        }else{
            QueryWrapper<OrderShopMaster> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("order_master_id", order.getId());
            OrderShopMaster update = new OrderShopMaster();
            update.setCurrentState(SystemConfig.ORDER_STATUS_DELIVER);
            shopMasterService.update(update, queryWrapper2);
        }

        List<Long> list = shopMasterService.listOrderShopMasterId(order.getId());
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (Long orderId : list) {
            // 商品货品数量增加
            QueryWrapper orderDetailWrapper = new QueryWrapper();
            orderDetailWrapper.eq("order_id",orderId);
            orderDetails.addAll(orderDetailService.list(orderDetailWrapper));
        }
        //商品数量/库存减少
        List<SkuAddStockVo> skuAddStockVos = new ArrayList<>();
        for(OrderDetail orderGoods : orderDetails){
            SkuAddStockVo skuAddStockVo = new SkuAddStockVo();
            skuAddStockVo.setCount(orderGoods.getGoodsCount());
            skuAddStockVo.setSkuId(orderGoods.getSkuId());
            skuAddStockVos.add(skuAddStockVo);

            //redis 拼团人数减一
            Stream.iterate(1, k -> ++k)
                    .limit(orderGoods.getGoodsCount())
                    .forEach(item -> {
                        redisService.decrement(SystemConfig.GROUP_IS_FULL + order.getGrouponOrderId(),1);
                    });
        }
        int addSubStock = orderService.addAndSubSkuStock(skuAddStockVos,true,false,true);
        if(addSubStock - 0 == 0){
            throw new RuntimeException("商品货品库存增加失败");
        }
        logger.info("系统结束处理延时任务---订单超时未付款---" + this.orderId);
    }
}
