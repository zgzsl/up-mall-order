package com.zsl.upmall.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zsl.upmall.config.SystemConfig;
import com.zsl.upmall.entity.GrouponActivities;
import com.zsl.upmall.entity.GrouponOrder;
import com.zsl.upmall.entity.OrderMaster;
import com.zsl.upmall.service.GrouponActivitiesService;
import com.zsl.upmall.service.GrouponOrderService;
import com.zsl.upmall.service.OrderMasterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class TaskStartupRunner implements ApplicationRunner {
     private final Logger logger = LoggerFactory.getLogger(TaskStartupRunner.class);

    @Autowired
    private OrderMasterService orderService;

    @Autowired
    private GrouponOrderService grouponOrderService;

    @Autowired
    private GrouponActivitiesService grouponActivitiesService;

    @Autowired
    private TaskService taskService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("========》》项目初始化扫描未处理的订单并添加进队列 ::: 开始《《==========");
        QueryWrapper<OrderMaster> orderQuery = new QueryWrapper<>();
        orderQuery.eq("order_status",SystemConfig.ORDER_STATUS_WAIT_PAY).eq("hidden",0);
        List<OrderMaster> orderList = orderService.list(orderQuery);
        for(OrderMaster order : orderList){
            LocalDateTime add = order.getCreateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expire =  add.plusMinutes(SystemConfig.ORDER_UNPAID / (60 *1000));
            if(expire.isBefore(now)) {
                // 已经过期，则加入延迟队列
                taskService.addTask(new OrderUnpaidTask(order.getId(), 0));
                logger.info("[[订单id："+order.getId()+"]],订单号：{{"+order.getSystemOrderNo()+"}}加入队列成功,延迟时间:===>"+0);
            }
            else{
                // 还没过期，则加入延迟队列
                long delay = ChronoUnit.MILLIS.between(now, expire);
                taskService.addTask(new OrderUnpaidTask(order.getId(), delay));
                logger.info("[[订单id："+order.getId()+"]],订单号：{{"+order.getSystemOrderNo()+"}}加入队列成功,延迟时间:===>"+delay);
            }
        }
        logger.info("========》》项目初始化扫描未处理的订单并添加进队列：：；完成《《==========");

        logger.info("【【【【【【【项目初始化扫描未处理的的拼团并添加进队列>>>>>>>开始】】】】】】");
        LambdaQueryWrapper<GrouponOrder> orderLambdaQueryWrapper = new LambdaQueryWrapper<>();
        orderLambdaQueryWrapper.isNull(GrouponOrder::getSettlementTime);
        List<GrouponOrder> grouponOrders = grouponOrderService.list(orderLambdaQueryWrapper);
        for(GrouponOrder grouponOrder : grouponOrders){
            LocalDateTime end = grouponOrder.getEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime now = LocalDateTime.now();
            GrouponActivities activitiesDetail = grouponActivitiesService.getById(grouponOrder.getGrouponActivitiesId());
            if(activitiesDetail == null){
                continue;
            }
            if(end.isBefore(now)) {
                // 已经过期，则加入延迟队列
                taskService.addTask(new GrouponOrderUnpaidTask(grouponOrder.getId(), activitiesDetail,0));
                logger.info("[[团队id："+grouponOrder.getId()+"]],团队订单号：{{"+grouponOrder.getGrouponOrderNo()+"}}加入队列成功,延迟时间:===>"+0);
            }
            else{
                // 还没过期，则加入延迟队列
                long delay = ChronoUnit.MILLIS.between(now, end);
                taskService.addTask(new GrouponOrderUnpaidTask(grouponOrder.getId(), activitiesDetail, delay));
                logger.info("[[团队id："+grouponOrder.getId()+"]],团队订单号：{{"+grouponOrder.getGrouponOrderNo()+"}}加入队列成功,延迟时间:===>"+delay);
            }
        }
        logger.info("【【【【【【【项目初始化扫描未处理的的拼团并添加进队列>>>>>结束】】】】】");

    }
}