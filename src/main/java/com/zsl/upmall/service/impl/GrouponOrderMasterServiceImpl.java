/**
 * @filename:GrouponOrderMasterServiceImpl 2020年04月08日
 * @project up-mall板根商城  V1.0
 * Copyright(c) 2018 binggleWang Co. Ltd.
 * All right reserved.
 */
package com.zsl.upmall.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeData;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.gson.JsonObject;
import com.zsl.upmall.config.SystemConfig;
import com.zsl.upmall.config.WebSocket;
import com.zsl.upmall.config.WxProperties;
import com.zsl.upmall.context.RequestContext;
import com.zsl.upmall.context.RequestContextMgr;
import com.zsl.upmall.entity.*;
import com.zsl.upmall.mapper.GrouponOrderMasterDao;
import com.zsl.upmall.service.*;
import com.zsl.upmall.task.GroupNoticeUnpaidTask;
import com.zsl.upmall.task.GrouponOrderUnpaidTask;
import com.zsl.upmall.task.TaskService;
import com.zsl.upmall.util.*;
import com.zsl.upmall.vo.BalacneRebateVo;
import com.zsl.upmall.vo.GroupOrderStatusEnum;
import com.zsl.upmall.vo.MiniNoticeVo;
import com.zsl.upmall.vo.SendMsgVo;
import com.zsl.upmall.vo.out.GrouponListVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description:TODO(服务实现)
 *
 * @version: V1.0
 * @author: binggleWang
 *
 */
@Service
public class GrouponOrderMasterServiceImpl extends ServiceImpl<GrouponOrderMasterDao, GrouponOrderMaster> implements GrouponOrderMasterService {

    private final Logger logger = LoggerFactory.getLogger(GrouponOrderMasterServiceImpl.class);

    @Autowired
    private WxProperties wxProperties;

    @Autowired
    private GrouponActivitiesService activitiesService;

    @Autowired
    private OrderMasterService orderMasterService;

    @Autowired
    private GrouponOrderMasterService grouponOrderMasterService;

    @Autowired
    private GrouponOrderService grouponOrderService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private OrderRefundService orderRefundService;

    @Override
    public IPage<GrouponListVo> getGrouponListByPage(IPage<GrouponListVo> page, Integer grouponOrderId) {
        return this.baseMapper.getGrouponListByPage(page, grouponOrderId);
    }

    /**
     *  场景说明抽奖结果通知
     * @param openId
     * @param pages 点击跳转页面
     * @param goodName 详细内容商品名称
     * @param orderSn 订单编号
     * @param totalFee 支付金额
     */
    @Override
    public void push(String openId,String pages,String goodName, String orderSn,String totalFee) {
        //1，配置
        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
        config.setAppid(wxProperties.getAppId());
        config.setSecret(wxProperties.getAppSecret());
        WxMaService wxMaService = new WxMaServiceImpl();
        wxMaService.setWxMaConfig(config);

        //2,推送消息
        WxMaSubscribeMessage subscribeMessage = WxMaSubscribeMessage.builder()
                .toUser(openId)
                .templateId(getTemplateNameId("draw"))
                .page(pages)
                .build();
        //创建一个参数集合
        ArrayList<WxMaSubscribeData> wxMaSubscribeData = new ArrayList<>();
        //第一个内容：
        WxMaSubscribeData wxMaSubscribeData1 = new WxMaSubscribeData();
        wxMaSubscribeData1.setName("thing2");
        wxMaSubscribeData1.setValue(goodName);
        wxMaSubscribeData.add(wxMaSubscribeData1);

        // 第二个内容：
        WxMaSubscribeData wxMaSubscribeData2 = new WxMaSubscribeData();
        wxMaSubscribeData2.setName("character_string6");
        wxMaSubscribeData2.setValue(orderSn);
        wxMaSubscribeData.add(wxMaSubscribeData2);

        // 第二个内容：
        WxMaSubscribeData wxMaSubscribeData3 = new WxMaSubscribeData();
        wxMaSubscribeData3.setName("amount3");
        wxMaSubscribeData3.setValue(totalFee);
        wxMaSubscribeData.add(wxMaSubscribeData3);

        // 第二个内容：
        WxMaSubscribeData wxMaSubscribeData4 = new WxMaSubscribeData();
        wxMaSubscribeData4.setName("thing9");
        wxMaSubscribeData4.setValue("您的拼团订单已“拼中”");
        wxMaSubscribeData.add(wxMaSubscribeData4);

        //把集合给大的data
        subscribeMessage.setData(wxMaSubscribeData);

        try {
            wxMaService.getMsgService().sendSubscribeMsg(subscribeMessage);
        } catch (Exception e) {
            logger.info("推送失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 场景说明拼团失败通知
     * @param openId
     * @param pages
     * @param goodName  详细内容商品名称
     * @param pinPrice 拼团价
     * @param refundFee 退款金额
     * @param notice 温馨提示
     */
    @Override
    public void push1(String openId,String pages,String goodName, String pinPrice,String refundFee,String notice) {
        //1，配置
        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
        config.setAppid(wxProperties.getAppId());
        config.setSecret(wxProperties.getAppSecret());
        WxMaService wxMaService = new WxMaServiceImpl();
        wxMaService.setWxMaConfig(config);

        //2,推送消息
        WxMaSubscribeMessage subscribeMessage = WxMaSubscribeMessage.builder()
                .toUser(openId)
                .templateId(getTemplateNameId("failed"))
                .page(pages)
                .build();
        //创建一个参数集合
        ArrayList<WxMaSubscribeData> wxMaSubscribeData = new ArrayList<>();
        //第一个内容：
        WxMaSubscribeData wxMaSubscribeData1 = new WxMaSubscribeData();
        wxMaSubscribeData1.setName("thing1");
        wxMaSubscribeData1.setValue(goodName);
        wxMaSubscribeData.add(wxMaSubscribeData1);

        // 第二个内容：
        WxMaSubscribeData wxMaSubscribeData2 = new WxMaSubscribeData();
        wxMaSubscribeData2.setName("amount2");
        wxMaSubscribeData2.setValue(pinPrice);
        wxMaSubscribeData.add(wxMaSubscribeData2);

        // 第二个内容：
        WxMaSubscribeData wxMaSubscribeData3 = new WxMaSubscribeData();
        wxMaSubscribeData3.setName("amount5");
        wxMaSubscribeData3.setValue(refundFee);
        wxMaSubscribeData.add(wxMaSubscribeData3);

        // 第二个内容：
        WxMaSubscribeData wxMaSubscribeData4 = new WxMaSubscribeData();
        wxMaSubscribeData4.setName("thing6");
        wxMaSubscribeData4.setValue(notice);
        wxMaSubscribeData.add(wxMaSubscribeData4);

        //把集合给大的data
        subscribeMessage.setData(wxMaSubscribeData);

        try {
            wxMaService.getMsgService().sendSubscribeMsg(subscribeMessage);
        } catch (Exception e) {
            logger.info("推送失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 场景说明拼团成功通知
     * @param openId
     * @param pages
     * @param goodName 详细内容商品名称
     * @param pinPrice 拼团价格
     * @param notice 温馨提示
     */
    @Override
    public void push2(String openId,String pages,String goodName,String pinPrice,String notice) {
        //1，配置
        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
        config.setAppid(wxProperties.getAppId());
        config.setSecret(wxProperties.getAppSecret());
        WxMaService wxMaService = new WxMaServiceImpl();
        wxMaService.setWxMaConfig(config);

        //2,推送消息
        WxMaSubscribeMessage subscribeMessage = WxMaSubscribeMessage.builder()
                .toUser(openId)
                .templateId(getTemplateNameId("success"))
                .page(pages)
                .build();
        //创建一个参数集合
        ArrayList<WxMaSubscribeData> wxMaSubscribeData = new ArrayList<>();
        //第一个内容：
        WxMaSubscribeData wxMaSubscribeData1 = new WxMaSubscribeData();
        wxMaSubscribeData1.setName("thing1");
        wxMaSubscribeData1.setValue(goodName);
        wxMaSubscribeData.add(wxMaSubscribeData1);

        // 第二个内容：
        WxMaSubscribeData wxMaSubscribeData3 = new WxMaSubscribeData();
        wxMaSubscribeData3.setName("amount5");
        wxMaSubscribeData3.setValue(pinPrice);
        wxMaSubscribeData.add(wxMaSubscribeData3);

        // 第二个内容：
        WxMaSubscribeData wxMaSubscribeData4 = new WxMaSubscribeData();
        wxMaSubscribeData4.setName("thing4");
        wxMaSubscribeData4.setValue(notice);
        wxMaSubscribeData.add(wxMaSubscribeData4);

        //把集合给大的data
        subscribeMessage.setData(wxMaSubscribeData);

        try {
            wxMaService.getMsgService().sendSubscribeMsg(subscribeMessage);
        } catch (Exception e) {
            logger.info("推送失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void doGrouponService(Long orderId,Integer userId) {
        //订单信息
        OrderMaster orderDetail = orderMasterService.getById(orderId);
        Integer grouponActivityId = orderDetail.getGrouponActivityId();
        Integer joinGroupId = orderDetail.getGrouponOrderId();

        //获取拼团活动信息
        GrouponActivities activityDetail = activitiesService.getById(grouponActivityId);
        if (activityDetail == null) {
            logger.info("拼团活动【【【" + grouponActivityId + "】】】不存在");
            return;
        }

        //判断活动是否过期
        LocalDateTime end = activityDetail.getEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime nowDate = LocalDateTime.now();
        if (end.isBefore(nowDate)) {
            logger.info("活动过期：【【【" + grouponActivityId + "】】】");
            return;
        }

        // 判断是否自己开团
        if (joinGroupId - 0 == 0) {
            //自己
            GrouponOrder grouponOrder = new GrouponOrder();
            LocalDateTime endDate = nowDate.plusMinutes(activityDetail.getExpireHour());
            //开始时间
            Date createTime = Date.from(nowDate.atZone(ZoneId.systemDefault()).toInstant());
            //结束时间
            Date endTime = null;
            if (endDate.isBefore(end)) {
                endTime = Date.from(endDate.atZone(ZoneId.systemDefault()).toInstant());
            } else {
                endTime = Date.from(end.atZone(ZoneId.systemDefault()).toInstant());
            }
            grouponOrder.setCreateTime(createTime)
                    .setEndTime(endTime)
                    .setGrouponActivitiesId(grouponActivityId)
                    .setGrouponCode(CharUtil.generateGrouponCode())
                    .setGrouponOrderNo(CharUtil.getCode(userId, activityDetail.getMode()))
                    .setGrouponOrderStatus(GroupOrderStatusEnum.HAVING.getCode())
                    .setUserMemberId(userId);
            if (!grouponOrderService.save(grouponOrder)) {
                logger.info("拼团主订单【【【" + grouponOrder.getGrouponOrderNo() + "】】】插入失败，用户id：【【【" + userId + "】】】");
                return;
            }
            joinGroupId = grouponOrder.getId();
            //开启延时队列 定时
            LocalDateTime endTimeLocal = endTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime createTimeLocal = createTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            long delay = ChronoUnit.MILLIS.between(createTimeLocal, endTimeLocal);
            taskService.addTask(new GrouponOrderUnpaidTask(joinGroupId,activityDetail,delay));

            doRedisGroupInfo(true,orderId,activityDetail.getMode(),grouponActivityId,activityDetail.getGroupCount(),joinGroupId,userId);
            //生成凭证放redis
            List<String> vouchers = CharUtil.generateJoinGroupCode(activityDetail.getGroupCount());
            redisService.lpushList(SystemConfig.GROUP_PREFIX + joinGroupId, vouchers);
            //如果抽奖团存放一个副本
            if (activityDetail.getMode() - 1 == 0) {
                Collections.shuffle(vouchers);
                redisService.lpushList("CP_" + SystemConfig.GROUP_PREFIX + joinGroupId, vouchers);
            }

        }else{
            // 存放拼团，活动redis 信息 （团队信息HASH: GROUPON_团队ID  用户ID  参与份额）
            doRedisGroupInfo(false,orderId,activityDetail.getMode(),grouponActivityId,activityDetail.getGroupCount(),joinGroupId,userId);
        }
        GrouponOrderMaster grouponOrderMaster = new GrouponOrderMaster();
        grouponOrderMaster.setMemberId(userId)
                .setOrderId(orderId.intValue())
                .setCreateTime(new Date())
                .setGrouponOrderId(joinGroupId);

        // 插入groupon-order-master
        insertGroupOrderMaster(grouponOrderMaster, joinGroupId, activityDetail.getMode(), userId, orderId);

        //webosocket通知
        SendMsgVo sendMsgVo =  sendMsg(orderId);
        WebSocket.sendMessageAll(JSON.toJSONString(sendMsgVo));

        //如果符合条件则 结算
        if (redisService.lsize(SystemConfig.GROUP_PREFIX + joinGroupId) - 0 == 0) {
            settlementGroup(joinGroupId,activityDetail);
        }
    }

    @Override
    public List<MiniNoticeVo> getGroupNoticeList(Long grouponOrderId, Integer grouponStatus) {
        return this.baseMapper.getGroupNoticeList(grouponOrderId,grouponStatus);
    }

    @Override
    public SendMsgVo sendMsg(Long orderId) {
        return this.baseMapper.sendMsg(orderId);
    }


    /**
     * 结算 （退款，和发放奖励金）
     * @param joinGroupId  拼团id
     * @param activityDetail  活动详情
     */
    @Override
    public void settlementGroup(Integer joinGroupId,GrouponActivities activityDetail){
        //判断是否处理过
        GrouponOrder isHandled = grouponOrderService.getById(joinGroupId);
        if(isHandled.getSettlementTime() != null){
            logger.info("拼团【【【【【"+joinGroupId+"】】】】】已经处理过");
            return ;
        }

        //删除redis zset group
        redisService.removeZset(SystemConfig.ACTIVE_INFO_PREFIX + activityDetail.getId(), joinGroupId+"");


        if(redisService.lsize(SystemConfig.GROUP_PREFIX + joinGroupId) - 0 > 0){
            // 时间到了，但是团没满，则 主订单拼团失败 ，退款
            GrouponOrder grouponOrderFail = new GrouponOrder();
            grouponOrderFail.setId(joinGroupId).setSettlementTime(new Date()).setGrouponOrderStatus(GroupOrderStatusEnum.FAILED.getCode());
            grouponOrderService.updateById(grouponOrderFail);

            //获取拼团所有订单
            LambdaQueryWrapper<GrouponOrderMaster> failOrderMaster = new LambdaQueryWrapper<>();
            failOrderMaster.eq(GrouponOrderMaster::getGrouponOrderId, joinGroupId);
            List<GrouponOrderMaster> allFailMasters = grouponOrderMasterService.list(failOrderMaster);

            List<GrouponOrderMaster> updateList = new ArrayList<>();
            for(GrouponOrderMaster grouponOrderMaster : allFailMasters){
                GrouponOrderMaster grouponOrderMasterUpdate = new GrouponOrderMaster();
                grouponOrderMasterUpdate.setId(grouponOrderMaster.getId());
                grouponOrderMasterUpdate.setGrouponStatus(GroupOrderStatusEnum.FAILED.getCode());
                OrderMaster orderDetail = orderMasterService.getById(grouponOrderMaster.getOrderId());
                if(orderDetail != null){
                    grouponOrderMasterUpdate.setGrouponResult("返回本金" + orderDetail.getPracticalPay()+"");
                }
                updateList.add(grouponOrderMasterUpdate);
            }
            boolean upresult = grouponOrderMasterService.updateBatchById(updateList);
            logger.info("团【【【【"+joinGroupId+"】】】】更新结果::"+upresult);

            //调用退款 接口 (微信退款和余额退款)
            List<OrderRefund> refundList = allFailMasters.stream()
                    .map(i -> {
                        OrderRefund orderRefund = new OrderRefund();
                        orderRefund.setCreateTime(new Date());
                        orderRefund.setOrderId(i.getOrderId());
                        orderRefund.setRefundDesc("拼团失败退款");
                        return orderRefund;
                    }).collect(Collectors.toList());
            orderRefundService.saveBatch(refundList);
            // todo 失败通知
            taskService.addTask(new GroupNoticeUnpaidTask(joinGroupId,60,GroupOrderStatusEnum.FAILED.getCode(),1));
            //余额退款扫描
            refundToBalance();
            return ;
        }

        // 修改 g-o 的结算时间  (主订单拼团成功)
        GrouponOrder grouponOrder = new GrouponOrder();
        grouponOrder.setId(joinGroupId).setSettlementTime(new Date()).setGrouponOrderStatus(GroupOrderStatusEnum.SUCCESS.getCode());
        grouponOrderService.updateById(grouponOrder);


        //去掉 延时队列
        taskService.removeTask(new GrouponOrderUnpaidTask(joinGroupId,activityDetail));

        //开始结算 修改子订单状态
        if (activityDetail.getMode() - 0 == 0) {
            LambdaQueryWrapper<GrouponOrderMaster> updateGroupOrderMasterQuery = new LambdaQueryWrapper<>();
            updateGroupOrderMasterQuery.eq(GrouponOrderMaster::getGrouponOrderId, joinGroupId);
            List<GrouponOrderMaster> grouponOrderMasterList = grouponOrderMasterService.list(updateGroupOrderMasterQuery);
            List<GrouponOrderMaster> updateList = new ArrayList<>();
            for(GrouponOrderMaster grouponOrderMaster : grouponOrderMasterList){
                GrouponOrderMaster grouponOrderMasterUpdate = new GrouponOrderMaster();
                grouponOrderMasterUpdate.setId(grouponOrderMaster.getId());
                grouponOrderMasterUpdate.setGrouponStatus(GroupOrderStatusEnum.SUCCESS.getCode());
                LambdaQueryWrapper<OrderDetail> detailLamb = new LambdaQueryWrapper<>();
                detailLamb.eq(OrderDetail::getOrderId,grouponOrderMaster.getOrderId()).last("limit 1");
                OrderDetail orderDetail = orderDetailService.getOne(detailLamb);
                if(orderDetail != null){
                    grouponOrderMasterUpdate.setGrouponResult(orderDetail.getGoodsName()+" *"+orderDetail.getGoodsCount());
                }
                updateList.add(grouponOrderMasterUpdate);
            }
            grouponOrderMasterService.updateBatchById(updateList);
        } else if (activityDetail.getMode() - 1 == 0) {
            final int final_joinGroupId = joinGroupId;
            // 获得 抽奖凭证(列表)（副本弹出来
            List<String> win_list = new ArrayList<>();
            Stream.iterate(1, k -> ++k)
                    .limit(activityDetail.getPrizeCount())
                    .forEach(item -> {
                        String voucherItem = redisService.lpop("CP_" + SystemConfig.GROUP_PREFIX + final_joinGroupId);
                        win_list.add(voucherItem);
                    });
            // 剩下凭证
            List<String> not_win_list = redisService.lgetall("CP_" + SystemConfig.GROUP_PREFIX + joinGroupId);

            // 未中奖的余额返利  （一部分平分，一部分随机发放）(丢redis)
            doRebateBalance(activityDetail.getBounty(), not_win_list.size(), joinGroupId);

            //修改状态
            LambdaQueryWrapper<GrouponOrderMaster> updateGroupOrderMasterQuery = new LambdaQueryWrapper<>();
            updateGroupOrderMasterQuery.eq(GrouponOrderMaster::getGrouponOrderId, joinGroupId);
            List<GrouponOrderMaster> allOrderMaster = grouponOrderMasterService.list(updateGroupOrderMasterQuery);
            List<GrouponOrderMaster> updateOrderMasterList = new ArrayList<>();
            List<Integer> splitOrderIds = new ArrayList<>();
            for (GrouponOrderMaster item : allOrderMaster) {
                GrouponOrderMaster updateItem = new GrouponOrderMaster();
                BeanUtils.copyProperties(item,updateItem);
                updateItem.setId(item.getId());
                int not_win_count = 0;
                List<String> voucherList = Arrays.asList(item.getVoucher().split(","));
                //中奖凭证
                StringBuilder win_voucher_str = new StringBuilder();
                BigDecimal backPrize = new BigDecimal(0);
                for (String vou : voucherList) {
                    if (not_win_list.contains(vou)) {
                        //没中奖的
                        not_win_count++;
                        String backPrizeItemStr = redisService.lpop(SystemConfig.NOT_WIN_LIST_PREFIX + joinGroupId);
                        logger.info("没中奖余额返利："+backPrizeItemStr);
                        backPrize = backPrize.add(new BigDecimal(backPrizeItemStr));
                    } else {
                        win_voucher_str.append(vou + ",");
                    }
                }
                //中奖了
                if (StringUtils.isNotBlank(win_voucher_str)) {
                    updateItem.setGrouponStatus(GroupOrderStatusEnum.SUCCESS.getCode());
                    LambdaQueryWrapper<OrderDetail> detailLamb = new LambdaQueryWrapper<>();
                    detailLamb.eq(OrderDetail::getOrderId,item.getOrderId()).last("limit 1");
                    OrderDetail orderDetail = orderDetailService.getOne(detailLamb);
                    if(orderDetail != null){
                        updateItem.setGrouponResult(orderDetail.getGoodsName()+" *"+orderDetail.getGoodsCount());
                    }
                    updateItem.setWinVoucher(win_voucher_str.toString().substring(0,win_voucher_str.toString().length() -1));
                } else {
                    updateItem.setGrouponStatus(GroupOrderStatusEnum.FAILED.getCode());
                }
                updateItem.setBackPrize(backPrize);
                item.setBackPrize(backPrize);
                updateOrderMasterList.add(updateItem);

                // 拆单
                if (not_win_count > 0 && not_win_count < voucherList.size()) {
                    BigDecimal refundPrice = splitOrder(item.getOrderId(), not_win_count);
                    splitOrderIds.add(item.getOrderId());
                    if(refundPrice != null){
                        updateItem.setGrouponResult("返回本金"+refundPrice+" + 奖励金" + backPrize + "");
                    }else{
                        updateItem.setGrouponResult("返回奖励金" + backPrize+ "");
                    }
                }else if(backPrize.compareTo(new BigDecimal(0)) > 0){
                    OrderMaster orderMaster = orderMasterService.getById(item.getOrderId());
                    if(orderMaster == null){
                        updateItem.setGrouponResult("查找不到原订单");
                    }else{
                        updateItem.setGrouponResult("返回本金"+orderMaster.getPracticalPay()+" + 奖励金" + backPrize + "");
                    }

                }
            }
            //修改group-order-master 状态
            grouponOrderMasterService.updateBatchById(updateOrderMasterList);

            //中奖通知 todo
            taskService.addTask(new GroupNoticeUnpaidTask(joinGroupId,60,GroupOrderStatusEnum.SUCCESS.getCode(),0));

            // 根据返利字段进行返利  (退款)
            updateOrderMasterList = updateOrderMasterList.stream()
                    .filter(item -> item.getBackPrize() != null && item.getBackPrize().compareTo(new BigDecimal(0)) > 0)
                    .collect(Collectors.toList());
            //调用返利接口 (发放余额)
            boolean balanceRebateResult =  HttpClientUtil.deductUserBalanceBatch(true,updateOrderMasterList,2);
            //调用退款 接口 (微信退款和余额退款)
            List<OrderRefund> refundList = updateOrderMasterList.stream()
                    .filter(item -> !splitOrderIds.contains(item.getOrderId()))
                    .map(i -> {
                        OrderRefund orderRefund = new OrderRefund();
                        orderRefund.setCreateTime(new Date());
                        orderRefund.setOrderId(i.getOrderId());
                        orderRefund.setRefundDesc("抽奖失败退款");
                        return orderRefund;
                    }).collect(Collectors.toList());
            orderRefundService.saveBatch(refundList);
            //拼团失败通知 todo
            //余额退款扫描
            refundToBalance();
        }
        //成功通知 todo
        taskService.addTask(new GroupNoticeUnpaidTask(joinGroupId,60,GroupOrderStatusEnum.SUCCESS.getCode(),2));
    }

    /**
     * 未中奖的余额返利  （一部分平分，一部分随机发放）
     * @param bounty  总的返利奖励金
     * @param notWinSize  没有中奖的凭证个数
     * @return
     */
    public void doRebateBalance(BigDecimal bounty, Integer notWinSize, Integer grouponOrderId) {
        logger.info("余额返利 notWinSize:"+notWinSize+",bounty:"+bounty+",grouponOrderId"+grouponOrderId);
        if(notWinSize - 1 == 0 ){
            List<String> redisNotWinList1 = new ArrayList<>();
            redisNotWinList1.add(bounty.toString());
            redisService.lpushList(SystemConfig.NOT_WIN_LIST_PREFIX + grouponOrderId, redisNotWinList1);
            return ;
        }
        //抽奖 (平分奖金)
        BigDecimal left = bounty.divide(new BigDecimal(2), 1);
        BigDecimal right = bounty.subtract(left);
        BigDecimal leftPointCount = new BigDecimal(notWinSize).divide(new BigDecimal(2), 1);
        BigDecimal rightPointCount = new BigDecimal(notWinSize).subtract(leftPointCount);
        BigDecimal leftAvg = left.divide(leftPointCount).setScale(2, BigDecimal.ROUND_DOWN);
        right = right.add(left.subtract(leftAvg.multiply(leftPointCount)));
        // 发送奖金到余额 (平均)
        List<BigDecimal> reusltLeft = Stream.iterate(1, k -> ++k)
                .limit(leftPointCount.intValue())
                .map(i -> leftAvg)
                .collect(Collectors.toList());
        // 随机发放
        List<Double> result = RedPacket.hb(right.doubleValue(), rightPointCount.intValue(), 0.01);
        List<BigDecimal> reusltRight = result.stream().map(item -> new BigDecimal(item).setScale(2, BigDecimal.ROUND_HALF_UP)).collect(Collectors.toList());
        List<BigDecimal> resultAll = new ArrayList<>();
        resultAll.addAll(reusltLeft);
        resultAll.addAll(reusltRight);
        List<String> redisNotWinList = resultAll.stream()
                .map(item -> item.toString()).collect(Collectors.toList());
        redisService.lpushList(SystemConfig.NOT_WIN_LIST_PREFIX + grouponOrderId, redisNotWinList);
    }


    /**
     * 拆单(将子订单拆成两部分，中奖和未中奖)
     * @param orderId
     * @param notWinCount
     */
    public BigDecimal splitOrder(Integer orderId, int notWinCount) {
        logger.info("没中奖数量："+notWinCount);
        LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderDetail::getOrderId, orderId);
        OrderDetail orderDetail = orderDetailService.getOne(queryWrapper);
        if (orderDetail == null) {
            return null;
        }
        //失败
        OrderDetail notWinOrderDetail = new OrderDetail();
        BeanUtils.copyProperties(orderDetail, notWinOrderDetail);
        BigDecimal not_win_price = orderDetail.getGoodsPrice().multiply(new BigDecimal(notWinCount));
        logger.info("没中奖数量原始："+not_win_price);
        OrderDetail winOrderDetail = new OrderDetail();
        winOrderDetail.setId(orderDetail.getId());
        winOrderDetail.setActualCount(orderDetail.getActualCount() - notWinCount);
        winOrderDetail.setGoodsCount(orderDetail.getGoodsCount() - notWinCount);
        winOrderDetail.setGoodsAmount(orderDetail.getGoodsAmount().subtract(not_win_price));
        winOrderDetail.setPracticalClearing(orderDetail.getPracticalClearing().subtract(not_win_price));
        winOrderDetail.setClearingInfo("1");
        orderDetailService.updateById(winOrderDetail);
        notWinOrderDetail.setPracticalClearing(not_win_price.add(notWinOrderDetail.getGoodsCarriage()));
        notWinOrderDetail.setGoodsAmount(not_win_price);
        notWinOrderDetail.setGoodsCount(notWinCount);
        notWinOrderDetail.setActualCount(notWinCount);
        notWinOrderDetail.setClearingInfo("0");
        orderDetailService.save(notWinOrderDetail);
        OrderRefund orderRefund = new OrderRefund();
        orderRefund.setCreateTime(new Date());
        orderRefund.setOrderId(orderId);
        orderRefund.setRefundDesc("订单部分凭证未中奖的退款");
        orderRefund.setTotalFee(Integer.parseInt(MoneyUtil.moneyYuan2FenStr(not_win_price)));
        orderRefund.setRefundFee(Integer.parseInt(MoneyUtil.moneyYuan2FenStr(not_win_price)));
        logger.info("没中奖数量腿狂："+orderRefund.getTotalFee());
        orderRefundService.save(orderRefund);
        return not_win_price;
    }

    /**
     * 第一次的时候 向redis添加zset 和hset
     * @param orderId
     * @param mode
     * @param grouponActivityId
     * @param grouponCount
     * @param joinGroupId
     * @param userId
     */
    public void doRedisGroupInfo(boolean isFirst, Long orderId, Integer mode, Integer grouponActivityId, Integer grouponCount, Integer joinGroupId, Integer userId) {
        LambdaQueryWrapper<OrderDetail> detailQueryWrapper = new LambdaQueryWrapper<>();
        detailQueryWrapper.eq(OrderDetail::getOrderId, orderId).last("limit 1");
        OrderDetail orderDetail = orderDetailService.getOne(detailQueryWrapper);
        if (detailQueryWrapper == null) {
            return;
        }
        if(isFirst){
            if (mode - 0 == 0) {
                BigDecimal score = new BigDecimal(1).divide(new BigDecimal(grouponCount)).multiply(new BigDecimal(100));
                redisService.zSet(SystemConfig.ACTIVE_INFO_PREFIX + grouponActivityId, joinGroupId+"", score.doubleValue());
                redisService.hset(SystemConfig.GROUP_INFO_PREFIX + joinGroupId, userId+"", 1+"");
            } else if (mode - 1 == 0) {
                BigDecimal score = new BigDecimal(orderDetail.getGoodsCount()).divide(new BigDecimal(grouponCount)).multiply(new BigDecimal(100));
                redisService.zSet(SystemConfig.ACTIVE_INFO_PREFIX + grouponActivityId, joinGroupId+"", score.doubleValue());
                redisService.hset(SystemConfig.GROUP_INFO_PREFIX + joinGroupId, userId+"", orderDetail.getGoodsCount()+"");
            }
        }else{
            int currentSize = 0;
            if(mode - 0 == 0){
                redisService.hset(SystemConfig.GROUP_INFO_PREFIX + joinGroupId, userId+"", 1+"");
                Map<Object, Object> hall = redisService.hgetall(SystemConfig.GROUP_INFO_PREFIX + joinGroupId);
                currentSize = hall.size();
             }else if(mode - 1 == 0){
                String old = redisService.hget(SystemConfig.GROUP_INFO_PREFIX + joinGroupId,userId+"");
                if("null".equals(old) || StringUtils.isBlank(old)){
                       old = "0";
                }
                redisService.hset(SystemConfig.GROUP_INFO_PREFIX + joinGroupId, userId+"",  orderDetail.getGoodsCount() + Integer.valueOf(old)+"");
                Map<Object, Object> hall = redisService.hgetall(SystemConfig.GROUP_INFO_PREFIX + joinGroupId);
                for(Map.Entry<Object, Object> o : hall.entrySet()){
                    currentSize += Integer.valueOf(o.getValue().toString());
                    System.out.println("djdj:"+o.getValue());
                }
            }

            BigDecimal score = new BigDecimal( currentSize).divide(new BigDecimal(grouponCount)).multiply(new BigDecimal(100));
            redisService.zUpdateScore(SystemConfig.ACTIVE_INFO_PREFIX + grouponActivityId, joinGroupId+"", score.doubleValue());
        }
    }


    /**
     * 插入grouponOrderMaster
     * @param grouponOrderMaster
     * @param groupOrderId
     * @param mode
     */
    public void insertGroupOrderMaster(GrouponOrderMaster grouponOrderMaster, Integer groupOrderId, Integer mode, Integer userId, Long orderId) {
        if (mode - 0 == 0) {
            // 判断用户是否参加当前团队，已参加则不再新增数据
            LambdaQueryWrapper<GrouponOrderMaster> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(GrouponOrderMaster::getMemberId, userId).eq(GrouponOrderMaster::getGrouponOrderId,groupOrderId);
            GrouponOrderMaster isExistOrderMaster = grouponOrderMasterService.getOne(queryWrapper);
            if (isExistOrderMaster != null) {
                return;
            }
            //普通
            String voucherItem = redisService.lpop(SystemConfig.GROUP_PREFIX + groupOrderId);
            grouponOrderMaster.setVoucher(voucherItem);
            grouponOrderMasterService.save(grouponOrderMaster);
        } else if (mode - 1 == 0) {
            LambdaQueryWrapper<OrderDetail> orderDetailQuery = new LambdaQueryWrapper<>();
            orderDetailQuery.eq(OrderDetail::getOrderId, orderId).last("limit 1");
            OrderDetail detail = orderDetailService.getOne(orderDetailQuery);
            if (detail == null) {
                return;
            }
            StringBuilder c_vouchers = new StringBuilder();
            Stream.iterate(1, k -> ++k)
                    .limit(detail.getGoodsCount())
                    .forEach(item -> {
                        String voucherItem = redisService.lpop(SystemConfig.GROUP_PREFIX + groupOrderId);
                        c_vouchers.append(voucherItem + ",");
                    });
            grouponOrderMaster.setVoucher(c_vouchers.toString().substring(0,c_vouchers.toString().length() - 1));
            grouponOrderMasterService.save(grouponOrderMaster);
        }
    }


    /**
     * 余额退款，（立即退）
     */
    public void refundToBalance(){
        logger.info("余额退款开始：");
        LambdaQueryWrapper<OrderRefund> orderRefundQuery = new LambdaQueryWrapper<>();
        orderRefundQuery.isNull(OrderRefund::getRefundTime);
        List<OrderRefund> refundList = orderRefundService.list(orderRefundQuery);
        List<OrderRefund> updateBatch = new ArrayList<>();
        List<GrouponOrderMaster> grouponOrderMasterList = new ArrayList<>();
        for(OrderRefund item : refundList){
            OrderRefund updateRefund = new OrderRefund();
            GrouponOrderMaster grouponOrderMaster = new GrouponOrderMaster();
            updateRefund.setId(item.getId());
            OrderMaster orderMaster = orderMasterService.getById(item.getOrderId());

            if(item.getTotalFee() == null || item.getTotalFee() - 0 == 0){}

            if(orderMaster != null && orderMaster.getPayWay() - 3 == 0){
                //微信
                grouponOrderMaster.setMemberId(orderMaster.getMemberId());
                grouponOrderMaster.setOrderId(orderMaster.getId().intValue());

                updateRefund.setOutRefundNo(CharUtil.getCode(orderMaster.getMemberId(),3));
                updateRefund.setOutTradeNo(orderMaster.getSystemOrderNo());
                updateRefund.setTransactionId(orderMaster.getTransactionOrderNo());
                if(item.getTotalFee() == null || item.getTotalFee() - 0 == 0){
                    updateRefund.setTotalFee(Integer.valueOf(MoneyUtil.moneyYuan2FenStr(orderMaster.getPracticalPay())));
                    updateRefund.setRefundFee(Integer.valueOf(MoneyUtil.moneyYuan2FenStr(orderMaster.getPracticalPay())));
                    grouponOrderMaster.setBackPrize(MoneyUtil.moneyFen2Yuan(updateRefund.getTotalFee().toString()));
                }else{
                    grouponOrderMaster.setBackPrize(MoneyUtil.moneyFen2Yuan(item.getTotalFee().toString()));
                }
                updateRefund.setRefundTime(new Date());

                grouponOrderMasterList.add(grouponOrderMaster);
                updateBatch.add(updateRefund);
            }else if(orderMaster != null && orderMaster.getPayWay() - 2 == 0){
                if(item.getTotalFee() == null || item.getTotalFee() - 0 == 0){
                    //设置成退款中 （微信）
                    // 将订单设置成 退款中
                    OrderMaster updateRefunding = new OrderMaster();
                    updateRefunding.setId(orderMaster.getId());
                    updateRefunding.setRefundTime(new Date());
                    updateRefunding.setOrderStatus(SystemConfig.ORDER_STATUS_REFUNDINGD);
                    boolean isSuccess = orderMasterService.updateById(updateRefunding);
                    logger.info("微信退款订单：【【【【"+orderMaster.getSystemOrderNo()+"】】】，修改结果：[[[["+isSuccess+"]]]]");
                }else{
                    logger.info("微信退款订单拆单：【【【【"+orderMaster.getSystemOrderNo());
                }
            }
        }
        boolean result = HttpClientUtil.deductUserBalanceBatch(false,grouponOrderMasterList,3);
        logger.info("余额退款结果："+result);
        if(result){
            orderRefundService.updateBatchById(updateBatch);
        }
    }

    /**
     * 获取模板id
     * @param templateName
     * @return
     */
    public String getTemplateNameId(String templateName) {
        Map<String, String> template = wxProperties.getTemplate().stream()
                .filter(item -> item.get("name").equals(templateName)).findAny().orElse(null);
        if (template != null) {
            return template.get("templateId");
        } else {
            return "";
        }
    }

}