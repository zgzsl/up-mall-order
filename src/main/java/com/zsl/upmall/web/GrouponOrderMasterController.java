package com.zsl.upmall.web;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.stream.JsonReader;
import com.zsl.upmall.aid.JsonResult;
import com.zsl.upmall.aid.PageParam;
import com.zsl.upmall.config.SynQueryDemo;
import com.zsl.upmall.config.SystemConfig;
import com.zsl.upmall.context.RequestContext;
import com.zsl.upmall.context.RequestContextMgr;
import com.zsl.upmall.entity.*;
import com.zsl.upmall.service.*;
import com.zsl.upmall.task.GroupNoticeUnpaidTask;
import com.zsl.upmall.task.GrouponOrderUnpaidTask;
import com.zsl.upmall.task.TaskService;
import com.zsl.upmall.util.CharUtil;
import com.zsl.upmall.util.HttpClientUtil;
import com.zsl.upmall.util.MoneyUtil;
import com.zsl.upmall.validator.RequestLimit;
import com.zsl.upmall.vo.GroupOrderStatusEnum;
import com.zsl.upmall.vo.out.BuyLimitVo;
import com.zsl.upmall.vo.out.GrouponListVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName GrouponOrderMasterController
 * @Description 参团列表
 * @Author binggleW
 * @Date 2020-05-13 10:37
 * @Version 1.0
 **/
@RestController
@RequestMapping("groupon")
public class GrouponOrderMasterController {
    private final Logger logger = LoggerFactory.getLogger(GrouponOrderMasterController.class);

    @Autowired
    private GrouponOrderMasterService grouponOrderMasterService;

    @Autowired
    private GrouponOrderService grouponOrderService;

    @Autowired
    private GrouponActivitiesService activitiesService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private OrderRefundService orderRefundService;

    @Autowired
    private OrderMasterService orderMasterService;

    @Autowired
    private TrackingService trackingService;

    @GetMapping("list")
    public JsonResult list(PageParam param, Integer grouponOrderId){
        JsonResult result = new JsonResult();
        Page<GrouponListVo> page = new Page(param.getPageNum(), param.getPageSize());
        return result.success(grouponOrderMasterService.getGrouponListByPage(page,grouponOrderId));
    }


    @GetMapping("resetRedis")
    public JsonResult resetRedis(Integer grouponId){
        JsonResult resetResult = new JsonResult();
        redisService.set(SystemConfig.GROUP_IS_FULL + grouponId,"1");
        return resetResult.success("重置成功");
    }

    @RequestLimit(count = 2, time = 180)
    @GetMapping("resetGrouponTime")
    public JsonResult resetGrouponTime(Integer groupActivityId){
        JsonResult resetResult = new JsonResult();
        //获取拼团活动信息
        GrouponActivities activityDetail = activitiesService.getById(groupActivityId);
        if (activityDetail == null) {
            logger.info("【设置活动】 拼团活动【【【" + groupActivityId + "】】】不存在");
            return resetResult.error("【设置活动】 拼团活动【【【" + groupActivityId + "】】】不存在");
        }

        //判断活动是否过期
        LocalDateTime end = activityDetail.getEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime nowDate = LocalDateTime.now();
        if (end.isBefore(nowDate)) {
            logger.info("【设置活动】活动过期：【【【" + groupActivityId + "】】】");
            return resetResult.error("【设置活动】活动过期：【【【" + groupActivityId + "】】】");
        }

        //根据活动id获取所有拼团  并且修改结束时间 ,重新加入队列
        List<GrouponOrder> grouponOrderList = grouponOrderService.list(
                Wrappers.<GrouponOrder>lambdaQuery().
                        eq(GrouponOrder::getGrouponActivitiesId,groupActivityId)
        );
        grouponOrderList.stream()
                .forEach(item -> {
                    GrouponOrder grouponOrder = new GrouponOrder();
                    LocalDateTime groupStartTime = item.getCreateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    LocalDateTime endDate = groupStartTime.plusMinutes(activityDetail.getExpireHour());
                    //结束时间
                    Date endTime = null;
                    if (endDate.isBefore(end)) {
                        endTime = Date.from(endDate.atZone(ZoneId.systemDefault()).toInstant());
                    } else {
                        endTime = Date.from(end.atZone(ZoneId.systemDefault()).toInstant());
                    }
                    grouponOrder.setId(item.getId());
                    grouponOrder.setEndTime(endTime);
                    grouponOrderService.updateById(grouponOrder);

                    //去掉 延时队列
                    taskService.removeTask(new GrouponOrderUnpaidTask(item.getId(),activityDetail));


                    //开启延时队列 定时
                    LocalDateTime endTimeLocal = endTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    LocalDateTime createTimeLocal = LocalDateTime.now();
                    long delay = ChronoUnit.MILLIS.between(createTimeLocal, endTimeLocal);
                    taskService.addTask(new GrouponOrderUnpaidTask(item.getId(),activityDetail,delay));
                });
        return resetResult.success("设置成功");
    }


    //根据用户id和supid获取限购数量
    @GetMapping("isBuyLimit")
    public JsonResult isBuyLimit(Integer spuId){
        RequestContext requestContext = RequestContextMgr.getLocalContext();
        Integer memberId = requestContext.getUserId();
        JsonResult result = new JsonResult();
        List<Integer> spuList = new ArrayList<>();
        spuList.add(spuId);
        List<BuyLimitVo>  buyLimitVos = orderMasterService.isBuyLimit(memberId,spuList);
        if(CollectionUtil.isNotEmpty(buyLimitVos)){
            return result.success(buyLimitVos.get(0).getLimits());
        }else{
            return result.error("获取限购数量错误");
        }
    }

    @GetMapping("test")
    public JsonResult test(Integer joinGroupId){
        JsonResult result = new JsonResult();
        GrouponOrder grouponOrder = grouponOrderService.getById(joinGroupId);
        if(grouponOrder == null){
            return result.error("拼团不存在");
        }
        GrouponActivities activities = activitiesService.getById(grouponOrder.getGrouponActivitiesId());
        if(activities == null){
            return result.error("拼团活动不存在");
        }
        grouponOrderMasterService.settlementGroup(joinGroupId,activities);
        return result.success(null);
    }

    @GetMapping("push")
    public JsonResult pushMessage(Long groupOrderId,Integer status,Integer type){
        JsonResult result = new JsonResult();
        taskService.addTask(new GroupNoticeUnpaidTask(groupOrderId,60,status,type));
        return result.success(null);
    }

    @GetMapping("shoudong")
    public JsonResult shoudong(){
        JsonResult result = new JsonResult();
        taskEndFissionWanted();
        return result.success(null);
    }


    @GetMapping("autoCom")
    public  JsonResult autoCompany(String trackingSn){
        JsonResult result = new JsonResult();
        String companyCode = new SynQueryDemo().getAutoCompany(trackingSn);
        Tracking tracking = trackingService.getOne(Wrappers.<Tracking>query()
            .lambda().eq(Tracking::getTrackingCode,companyCode)
        );
        return result.success(tracking);
    }


    @GetMapping("deliver")
    public JsonResult deliver(String orderSn,String trackingSn,Integer trackingCompanyId){
        logger.info("订单号:【"+orderSn+"】,物流号+【"+trackingSn+"】,物流公司【"+trackingCompanyId+"】");
        JsonResult result = new JsonResult();
        OrderMaster orderMaster = orderMasterService.getOne(Wrappers.<OrderMaster>query()
            .lambda().eq(OrderMaster::getSystemOrderNo,orderSn)
        );
        if(orderMaster == null ){
            return result.error("订单【"+orderSn+"】不存在");
        }
        if(orderMaster.getOrderStatus() - SystemConfig.ORDER_STATUS_DELIVER != 0){
            return result.error("订单【"+orderSn+"】状态不对");
        }
        OrderMaster update = new OrderMaster();
        update.setId(orderMaster.getId());
        update.setTrackingNumber(trackingSn);
        update.setTrackingCompanyId(trackingCompanyId);
        update.setDeliverTime(new Date());
        update.setOrderStatus(SystemConfig.ORDER_STATUS_RECIEVE);
        if(!orderMasterService.updateById(update)){
            return result.error("发货失败");
        }
        return result.success("发货成功");
    }

    public void taskEndFissionWanted(){
        logger.info("微信退款手动任务=======》开始");
        //每天凌晨00:15触发
        LambdaQueryWrapper<OrderRefund> orderRefundQuery = new LambdaQueryWrapper<>();
        orderRefundQuery.isNull(OrderRefund::getRefundTime);
        List<OrderRefund> refundList = orderRefundService.list(orderRefundQuery);
        List<OrderRefund> updateBatch = new ArrayList<>();
        for(OrderRefund item : refundList){
            //判断时间 (测试先放开)
           /* LocalDateTime add = item.getCreateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime now = DateUtil.getCurrent14().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime expire =  now.plusDays(1);
            if(add.isBefore(expire)){
                continue;
            }*/

            OrderRefund updateRefund = new OrderRefund();
            updateRefund.setId(item.getId());
            updateRefund.setRefundDesc(item.getRefundDesc());
            OrderMaster orderMaster = orderMasterService.getById(item.getOrderId());

            if(orderMaster != null && orderMaster.getPayWay() - 2 == 0){
                //微信
                updateRefund.setOutRefundNo(CharUtil.getCode(orderMaster.getMemberId(),3));
                updateRefund.setOutTradeNo(orderMaster.getSystemOrderNo());
                updateRefund.setTransactionId(orderMaster.getTransactionOrderNo());
                if(item.getTotalFee() == null || item.getTotalFee() - 0 == 0){
                    updateRefund.setTotalFee(Integer.valueOf(MoneyUtil.moneyYuan2FenStr(orderMaster.getPracticalPay())));
                    updateRefund.setRefundFee(Integer.valueOf(MoneyUtil.moneyYuan2FenStr(orderMaster.getPracticalPay())));
                }else{
                    updateRefund.setTotalFee(item.getTotalFee());
                    updateRefund.setRefundFee(item.getRefundFee());
                }
                // 调用 微信退款
                boolean result =  HttpClientUtil.doRefund(updateRefund);
                if(result){
                    updateRefund.setRefundTime(new Date());
                }
                updateBatch.add(updateRefund);
            }
        }
        boolean result = orderRefundService.updateBatchById(updateBatch);
        logger.info("微信退款手动任务=======》结束+参数【【【"+updateBatch+"】】】结果："+result);
    }
}
