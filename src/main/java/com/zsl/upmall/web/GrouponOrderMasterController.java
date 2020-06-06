package com.zsl.upmall.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.stream.JsonReader;
import com.zsl.upmall.aid.JsonResult;
import com.zsl.upmall.aid.PageParam;
import com.zsl.upmall.entity.OrderMaster;
import com.zsl.upmall.entity.OrderRefund;
import com.zsl.upmall.service.GrouponOrderMasterService;
import com.zsl.upmall.service.OrderMasterService;
import com.zsl.upmall.service.OrderRefundService;
import com.zsl.upmall.task.GroupNoticeUnpaidTask;
import com.zsl.upmall.task.TaskService;
import com.zsl.upmall.util.CharUtil;
import com.zsl.upmall.util.HttpClientUtil;
import com.zsl.upmall.util.MoneyUtil;
import com.zsl.upmall.vo.GroupOrderStatusEnum;
import com.zsl.upmall.vo.out.GrouponListVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private TaskService taskService;

    @Autowired
    private OrderRefundService orderRefundService;

    @Autowired
    private OrderMasterService orderMasterService;

    @GetMapping("list")
    public JsonResult list(PageParam param, Integer grouponOrderId){
        JsonResult result = new JsonResult();
        Page<GrouponListVo> page = new Page(param.getPageNum(), param.getPageSize());
        return result.success(grouponOrderMasterService.getGrouponListByPage(page,grouponOrderId));
    }

    @GetMapping("test")
    public JsonResult test(Long orderId,Integer userId){
        JsonResult result = new JsonResult();
        grouponOrderMasterService.doGrouponService(orderId,userId);
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
                    updateRefund.setRefundFee(item.getTotalFee());
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
