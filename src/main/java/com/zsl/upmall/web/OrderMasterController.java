/**
 * @filename:OrderMasterController 2020年04月08日
 * @project up-mall板根商城  V1.0
 * Copyright(c) 2020 binggleWang Co. Ltd.
 * All right reserved.
 */
package com.zsl.upmall.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsl.upmall.aid.JsonResult;
import com.zsl.upmall.aid.PageParam;
import com.zsl.upmall.config.SynQueryDemo;
import com.zsl.upmall.config.SystemConfig;
import com.zsl.upmall.context.RequestContext;
import com.zsl.upmall.context.RequestContextMgr;
import com.zsl.upmall.entity.*;
import com.zsl.upmall.service.*;
import com.zsl.upmall.task.OrderUnpaidTask;
import com.zsl.upmall.task.TaskService;
import com.zsl.upmall.util.DateUtil;
import com.zsl.upmall.util.HttpClientUtil;
import com.zsl.upmall.util.IpUtil;
import com.zsl.upmall.util.MoneyUtil;
import com.zsl.upmall.vo.BalanceRefundListVo;
import com.zsl.upmall.vo.BalanceRefundVo;
import com.zsl.upmall.vo.RefundNotifyVo;
import com.zsl.upmall.vo.in.*;
import com.zsl.upmall.vo.out.Logistics;
import com.zsl.upmall.vo.out.OrderListVo;
import com.zsl.upmall.vo.out.UnifiedOrderVo;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>自动生成工具：mybatis-dsc-generator</p>
 *
 * <p>说明： API接口层</P>
 * @version: V1.0
 * @author: binggleWang
 * @time 2020年04月08日
 *
 */
@RestController
@RequestMapping("/order")
public class OrderMasterController {
    private final Logger logger = LoggerFactory.getLogger(OrderMasterController.class);

    @Autowired
    protected OrderMasterService baseService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private UserAddressService addressService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TrackingService trackingService;

    @Autowired
    private SkuGrouponPriceService skuGrouponPriceService;

    @Autowired
    private GrouponOrderMasterService grouponOrderMasterService;

    @Autowired
    private GrouponActivitiesService grouponActivitiesService;

    @Autowired
    private GrouponOrderService grouponOrderService;

    protected JsonResult result = new JsonResult();


    /**
     * @explain 订单详情
     * @param   id 订单id
     * @return JsonResult
     * @author binggleWang
     * @time 2019年10月16日
     */
    @GetMapping("/getById/{id}")
    public JsonResult getById(@PathVariable("id") Long id) {
        Map<String, Object> orderInfo = new HashMap<String, Object>();
        OrderMaster orderMaster = baseService.getById(id);
        if (orderMaster == null) {
            return result.error("订单不存在");
        }
        orderInfo.put("id", id);
        orderInfo.put("orderSn", orderMaster.getSystemOrderNo());
        orderInfo.put("traceNo", orderMaster.getTransactionOrderNo());
        orderInfo.put("payWay", orderMaster.getPayWay());
        orderInfo.put("comboLevel", orderMaster.getComboLevel());
        orderInfo.put("goodsAmount", orderMaster.getTotalGoodsAmout());
        orderInfo.put("actualPrice", orderMaster.getPracticalPay());
        orderInfo.put("freightPrice", orderMaster.getTotalCarriage());
        orderInfo.put("status", orderMaster.getOrderStatus());
        orderInfo.put("statusText", SystemConfig.getStatusText(orderMaster.getOrderStatus()));
        orderInfo.put("submitTime", DateUtil.DateToString(orderMaster.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
        orderInfo.put("payTime", DateUtil.DateToString(orderMaster.getPayTime(), "yyyy-MM-dd HH:mm:ss"));
        orderInfo.put("finishTime", DateUtil.DateToString(orderMaster.getFinishedTime(), "yyyy-MM-dd HH:mm:ss"));
        orderInfo.put("cancelTime", DateUtil.DateToString(orderMaster.getCancelTime(), "yyyy-MM-dd HH:mm:ss"));
        orderInfo.put("deliverTime",DateUtil.DateToString(orderMaster.getDeliverTime(), "yyyy-MM-dd HH:mm:ss"));
        orderInfo.put("refundTime",DateUtil.DateToString(orderMaster.getRefundTime(), "yyyy-MM-dd HH:mm:ss"));
        orderInfo.put("refundFinishTime",DateUtil.DateToString(orderMaster.getRefundFinishTime(), "yyyy-MM-dd HH:mm:ss"));
        orderInfo.put("expire_time", orderMaster.getCreateTime().getTime() / 1000 + SystemConfig.ORDER_UNPAID / 1000);
        orderInfo.put("shareId", orderMaster.getRemark());

        //订单商品列表
        List<SkuDetailVo> productDetailList = new ArrayList<>();
        QueryWrapper orderDetailWrapper = new QueryWrapper();
        orderDetailWrapper.eq("order_id", id);
        List<OrderDetail> orderDetails = orderDetailService.list(orderDetailWrapper);

        for (OrderDetail orderGoods : orderDetails) {
            SkuDetailVo skuDetailVo = new SkuDetailVo();
            skuDetailVo.setSkuPrice(orderGoods.getGoodsPrice());
            skuDetailVo.setProductCount(orderGoods.getGoodsCount());
            skuDetailVo.setSkuImage(orderGoods.getGoodsImg());
            skuDetailVo.setSkuName(orderGoods.getGoodsName());
            skuDetailVo.setSpec(orderGoods.getGoodsSpec());
            skuDetailVo.setSkuId(orderGoods.getSkuId());
            skuDetailVo.setDesc(orderGoods.getClearingInfo());
            productDetailList.add(skuDetailVo);
        }
        orderInfo.put("productDetailList", productDetailList);
        //订单地址信息
        AddressInfo addressInfo = addressService.addressInfo(new Long(orderMaster.getAddressId()));
        orderInfo.put("addressInfo", addressInfo);
        return result.success(orderInfo);
    }

    /**
     * @explain 下订单
     * @param   orderInfo 订单信息
     * @return Boolean
     * @author binggleWang
     * @time 2019年10月16日
     */
    @PostMapping("/createOrder")
    public JsonResult createOrder(@RequestBody CreateOrderVo orderInfo, HttpServletRequest request) {
        long startTime = System.currentTimeMillis();
        //获取用户 userId
        RequestContext requestContext = RequestContextMgr.getLocalContext();
        Integer userId = requestContext.getUserId();
        if (userId == null) {
            return result.error("用户信息错误");
        }

        // 根据 是否拼团，拼团是否过期
        if(orderInfo.getGrouponActivityId() != null && orderInfo.getGrouponActivityId() - 0 != 0 && orderInfo.getJoinGroupId() - 0 != 0){
            LocalDateTime nowDate = LocalDateTime.now();
            GrouponOrder grouponOrder = grouponOrderService.getById(orderInfo.getJoinGroupId());
            if(grouponOrder != null ){
                LocalDateTime endTime = grouponOrder.getEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                if(grouponOrder.getSettlementTime() != null || endTime.isBefore(nowDate)){
                    return result.error("拼团活动结束");
                }
            }else{
                return result.error("拼团不存在");
            }
            //获取拼团活动信息
            GrouponActivities activityDetail = grouponActivitiesService.getById(orderInfo.getGrouponActivityId());
            if (activityDetail == null) {
                logger.info("下单拼团活动【【【" + orderInfo.getGrouponActivityId() + "】】】不存在");
                return result.error("拼团活动不存在");
            }

            //判断活动是否过期
            LocalDateTime end = activityDetail.getEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            if (end.isBefore(nowDate)) {
                logger.info("下单活动过期：【【【" + orderInfo.getGrouponActivityId() + "】】】");
                return result.error("活动过期");
            }
        }

        //创建订单
        Long orderId = null;
        //订单号
        String orderSn = "";
        // 支付方式
        int payType = 0;
        //去支付（）
        OrderMaster toPayOrder = null;
        if (StringUtils.isNotBlank(orderInfo.getOrderSn())) {
            QueryWrapper<OrderMaster> toPayOrderQuery = new QueryWrapper<>();
            toPayOrderQuery.eq("system_order_no", orderInfo.getOrderSn()).eq("hidden", 0).last("LIMIT 1");
            toPayOrder = baseService.getOne(toPayOrderQuery);
            if (toPayOrder == null) {
                return result.error("订单不存在");
            }
            if (toPayOrder.getOrderStatus() - SystemConfig.ORDER_STATUS_WAIT_PAY != 0) {
                return result.error("订单状态错误");
            }
            orderSn = toPayOrder.getSystemOrderNo();
            orderId = toPayOrder.getId();
            payType = toPayOrder.getPayWay();
        }

        if (StringUtils.isBlank(orderInfo.getOrderSn())) {
            if (orderInfo == null) {
                return result.error("订单参数错误");
            }

            if (orderInfo.getCartId() == null || orderInfo.getAddressId() == null) {
                return result.error("参数错误");
            }
            // 收货地址
            // AddressInfo addressInfo = HttpClientUtil.getAddressInfoById(orderInfo.getAddressId(),requestContext.getToken());
            if (orderInfo.getAddressId() == null) {
                return result.error("地址不存在");
            }

            // 商品价格
            List<OrderProductVo> orderProductVoList = new ArrayList<>();
            SkuDetailVo sku = null;
            if (orderInfo.getCartId() - 0 == 0) {

                // 普通
                sku = baseService.getSkuDetail(orderInfo.getProductId());
                if (sku == null) {
                    return result.error("商品不存在或已下架");
                }
                if (sku.getStock() - orderInfo.getProductCount() < 0) {
                    return result.error(sku.getSkuName()+"库存不足");
                }
                BigDecimal skuPrice = null;
                if(orderInfo.getGrouponActivityId() - 0 != 0){
                    LambdaQueryWrapper<SkuGrouponPrice> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(SkuGrouponPrice::getSkuId,sku.getSkuId()).last("limit 1");
                    SkuGrouponPrice skuGrouponPrice = skuGrouponPriceService.getOne(queryWrapper);
                    if(skuGrouponPrice != null){
                        skuPrice = skuGrouponPrice.getGrouponPrice();
                    }
                }else{
                    skuPrice = baseService.getSkuPriceByUserLevel(userId, sku.getSkuId());
                }

                if (skuPrice == null) {
                    return result.error(sku.getSkuName()+"价格错误");
                }
                sku.setSkuPrice(skuPrice);
                //需要支付得 价格
                BigDecimal needTotalPrice = sku.getSkuPrice().multiply(new BigDecimal(orderInfo.getProductCount())).add(orderInfo.getFreight());
                if (needTotalPrice.compareTo(orderInfo.getTotalAmount()) != 0) {
                    return result.error("订单价格不一致");
                }
                orderProductVoList.add(new OrderProductVo(sku.getSkuId(), orderInfo.getProductCount(), sku.getSkuPrice(), sku.getSkuImage(), sku.getSpec(), sku.getSkuName()));

            } else {
                // 购物车 (又加上了)
                if(orderInfo.getCartIdList().isEmpty()){
                    return result.error("结算信息为空");
                }
                QueryWrapper<ShoppingCart> queryWrapper = new QueryWrapper<>();
                queryWrapper.in("id",orderInfo.getCartIdList());
                List<ShoppingCart> shoppingCarts = shoppingCartService.list(queryWrapper);
                if(shoppingCarts.isEmpty()){
                    return  result.error("购物车已经清空");
                }
                BigDecimal needTotalCartPrice = new BigDecimal(0);
                for(ShoppingCart cart : shoppingCarts){
                    sku = baseService.getSkuDetail(cart.getSkuId());
                    if (sku == null) {
                        return result.error("不存在或已下架");
                    }
                    if (sku.getStock() - cart.getGoodsNum() < 0) {
                        return result.error(sku.getSkuName()+"库存不足");
                    }
                    BigDecimal skuPrice = baseService.getSkuPriceByUserLevel(userId, sku.getSkuId());
                    if (skuPrice == null) {
                        return result.error(sku.getSkuName()+"价格错误");
                    }
                    sku.setSkuPrice(skuPrice);
                    BigDecimal itemPrice = sku.getSkuPrice().multiply(new BigDecimal(cart.getGoodsNum()));
                    needTotalCartPrice = needTotalCartPrice.add(itemPrice);
                    orderProductVoList.add(new OrderProductVo(sku.getSkuId(), cart.getGoodsNum(), sku.getSkuPrice(), sku.getSkuImage(), sku.getSpec(), sku.getSkuName()));
                }
                needTotalCartPrice = needTotalCartPrice.add(orderInfo.getFreight());
                if(needTotalCartPrice.compareTo(orderInfo.getTotalAmount()) != 0){
                    return result.error("订单价格不一致");
                }
                if(orderProductVoList.isEmpty()){
                    return result.error("请选择需要购买得商品");
                }
            }

            // 最终支付费用
            BigDecimal actualPrice = orderInfo.getTotalAmount();
            OrderMaster order = new OrderMaster();
            order.setAddressId(orderInfo.getAddressId());
            order.setHidden(0);
            order.setMemberId(userId);
            order.setPayWay(orderInfo.getPayWay());
            order.setComboLevel(orderInfo.getComboLevel());
            order.setPracticalPay(actualPrice);
            order.setTotalCarriage(orderInfo.getFreight());
            order.setShopId(0);
            //存放 分享码，拼团相关信息
            order.setGrouponActivityId(orderInfo.getGrouponActivityId());
            order.setGrouponOrderId(orderInfo.getJoinGroupId());
            order.setRemark(orderInfo.getShareId());
            order.setTotalGoodsAmout(orderInfo.getTotalAmount().subtract(orderInfo.getFreight()));
            //订单状态 待付款(状态)
            order.setOrderStatus(SystemConfig.ORDER_STATUS_WAIT_PAY);
            //订单号
            order.setSystemOrderNo(generateOrderSn(userId));
            order.setCreateTime(new Date());
            boolean isSaveSuccess = baseService.save(order);
            if (!isSaveSuccess) {
                return result.error("下单失败");
            }
            orderId = order.getId();
            orderSn = order.getSystemOrderNo();
            payType = order.getPayWay();
            if(!(orderInfo.getCartId() - 0 == 0)) {
                // 如果是购物车结算则清除购物车 (又加上上了 )
                orderInfo.getCartIdList().stream().forEach(item -> {
                    shoppingCartService.removeById(item);
                });
            }

            //下单才扣库存
            //商品数量/库存减少
            List<SkuAddStockVo> skuAddStockVos = new ArrayList<>();
            //订单详情
            for (OrderProductVo orderProductVo : orderProductVoList) {
                //判断库存是否足够
                SkuAddStockVo skuAddStockVo = new SkuAddStockVo();
                skuAddStockVo.setCount(orderProductVo.getProductCount());
                skuAddStockVo.setSkuId(orderProductVo.getSkuId());
                skuAddStockVos.add(skuAddStockVo);
                //订单详情
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setActualCount(orderProductVo.getProductCount());
                orderDetail.setGoodsCount(orderProductVo.getProductCount());
                orderDetail.setGoodsPrice(orderProductVo.getProductPrice());
                orderDetail.setGoodsImg(orderProductVo.getProductImg());
                orderDetail.setGoodsName(orderProductVo.getProductName());
                orderDetail.setGoodsSpec(orderProductVo.getSpec());
                orderDetail.setGoodsCarriage(orderInfo.getFreight());
                orderDetail.setOrderId(orderId);
                orderDetail.setSkuId(orderProductVo.getSkuId());
                orderDetail.setGoodsAmount(orderInfo.getTotalAmount().subtract(orderInfo.getFreight()));
                orderDetail.setPracticalClearing(orderInfo.getTotalAmount());
                orderDetailService.save(orderDetail);
            }

            int addSubStock = baseService.addAndSubSkuStock(skuAddStockVos, false,false,true);
            if (addSubStock - 0 == 0) {
                OrderMaster updateHidden = new OrderMaster();
                updateHidden.setId(order.getId());
                updateHidden.setHidden(1);
                baseService.updateById(updateHidden);
                return result.error("扣库存失败");
            }
            // 订单地址处理
            JsonResult updateAddreResult = setDeleteAndAdd(orderInfo.getAddressId());
            logger.info("订单模块：{{" + order.getSystemOrderNo() + "}}的地址处理结果=====》》》" + updateAddreResult);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        map.put("orderSn", orderSn);

        // 支付方式，添加余额支付
        if(payType - SystemConfig.WEIXIN_PAY == 0) {
            // 将元转分
            long startUnifiedTime = System.currentTimeMillis();
            String totalFee = "";
            if (StringUtils.isBlank(orderInfo.getOrderSn())) {
                totalFee = MoneyUtil.moneyYuan2FenStr(orderInfo.getTotalAmount());
            } else {
                totalFee = MoneyUtil.moneyYuan2FenStr(toPayOrder.getPracticalPay());
            }
            UnifiedOrderVo unifiedOrderVo = HttpClientUtil.unifiedOrder(IpUtil.getRequestIp(request), orderInfo.getOpenid(), "up-mall商品支付", orderSn, totalFee,requestContext.getToken());
            logger.info("订单模块：{{" + orderSn + "}}的微信统一下单结果=====》》》" + unifiedOrderVo);
            if (unifiedOrderVo == null) {
                return result.error("微信统一下单失败");
            }
            map.put("unifiedData", unifiedOrderVo.getData());
            logger.info("统一下单语句执行时间=========【【【 " + (System.currentTimeMillis() - startUnifiedTime) / 1000 + " 】】】秒");
        }else if(payType - SystemConfig.BALANCE_PAY == 0){
            //添加余额支付
            BigDecimal totalFeeBalance;
            if (StringUtils.isBlank(orderInfo.getOrderSn())) {
                totalFeeBalance = orderInfo.getTotalAmount();
            } else {
                totalFeeBalance = toPayOrder.getPracticalPay();
            }
            boolean balancePayResult = HttpClientUtil.deductUserBalance(4,false,userId,requestContext.getToken(),totalFeeBalance,orderSn);
            if(!balancePayResult){
                logger.info("订单模块：{{" + orderSn + "}}的余额支付结果=====》》》" + balancePayResult);
                return result.error("余额支付失败");
            }

        }else {
            return result.error("暂不支持该支付方式");
        }

        // 订单支付超期任务
        if (StringUtils.isBlank(orderInfo.getOrderSn())) {
            taskService.addTask(new OrderUnpaidTask(orderId));
        }

        logger.info("总下单模块执行时间=========【【【 " + (System.currentTimeMillis() - startTime) / 1000 + " 】】】秒");
        return result.success("下单成功", map);
    }

    /**
     * 生成订单号
     * @param userId
     * @return
     */
    public String generateOrderSn(Integer userId) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
        String now = df.format(LocalDate.now());
        String orderSn = now + getRandomNum(6);
        while (countByOrderSn(userId, orderSn) != 0) {
            orderSn = now + getRandomNum(6);
        }
        return orderSn;
    }

    public int countByOrderSn(Integer userId, String orderSn) {
        QueryWrapper<OrderMaster> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("member_id", userId).eq("system_order_no", orderSn);
        return baseService.count(queryWrapper);
    }

    private String getRandomNum(Integer num) {
        String base = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < num; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 将原来地址设置为假删除，并新增 一条一模一样的
     * @param addressId
     * @return
     */
    public JsonResult setDeleteAndAdd(Integer addressId) {
        UserAddress userAddress = addressService.getById(addressId);
        if (userAddress == null) {
            return result.error("地址不存在");
        }
        //设置为加删除
        UserAddress setDelete = new UserAddress();
        setDelete.setId(userAddress.getId());
        setDelete.setIsDelete(1);
        if (!addressService.updateById(setDelete)) {
            return result.error("修改失败");
        }
        //新增一条一模一样的
        if (!addressService.save(userAddress)) {
            return result.error("修改失败");
        }
        return result.success("修改成功");
    }


    /**
     * @explain 订单列表
     * @param   param ,others,对象参数：AppPage<UserFriend>
     * @return PageInfo<UserFriend>
     * @author binggleWang
     * @time 2019年10月16日
     */
    @GetMapping("/getOrderPages")
    public JsonResult<IPage<OrderListVo>> getPages(PageParam param, Integer orderStatus) {
        JsonResult<IPage<OrderListVo>> returnPage = new JsonResult<IPage<OrderListVo>>();
        //获取用户 userId
        RequestContext requestContext = RequestContextMgr.getLocalContext();
        Integer userId = requestContext.getUserId();
        Page<OrderListVo> page = new Page(param.getPageNum(), param.getPageSize());
        if (orderStatus == null) {
            //查询全部
            orderStatus = -1;
        }
        if ((orderStatus - SystemConfig.ORDER_STATUS_RECIEVE != 0) && (orderStatus - SystemConfig.ORDER_STATUS_WAIT_PAY != 0) && (orderStatus - SystemConfig.ORDER_STATUS_CANCLE != 0) && (orderStatus - SystemConfig.ORDER_STATUS_FINISH != 0) && (orderStatus - SystemConfig.ORDER_STATUS_DELIVER != 0)&& (orderStatus - SystemConfig.ORDER_STATUS_REFUNDINGD != 0)&& (orderStatus - SystemConfig.ORDER_STATUS_REFUNDED != 0)) {
            //查询全部
            orderStatus = -1;
        }
        //分页数据
        IPage<OrderListVo> pageData = baseService.getOrderListByStatus(page, orderStatus, userId);
        returnPage.success(pageData);

        return returnPage;
    }


    /**
     *  确认收货
     * @param id 订单id
     * @return 取消订单操作结果
     */
    @PostMapping("/updateOrderStatus/{id}")
    public JsonResult<IPage> updateOrderStatus(@PathVariable("id") Long id) {
        if (id == null) {
            return result.error("参数错误");
        }
        OrderMaster orderMaster = baseService.getById(id);
        if (orderMaster == null) {
            return result.error("订单不存在");
        }
        //判断订单状态是否为 待收货
        if (orderMaster.getOrderStatus() - SystemConfig.ORDER_STATUS_RECIEVE != 0) {
            return result.error("订单状态不对");
        }

        // 设置订单已完成
        OrderMaster updateStatus = new OrderMaster();
        updateStatus.setId(orderMaster.getId());
        updateStatus.setOrderStatus(SystemConfig.ORDER_STATUS_FINISH);
        updateStatus.setFinishedTime(new Date());
        if (!baseService.updateById(updateStatus)) {
            throw new RuntimeException("确认收货失败");
        }

        return result.success("确认收货成功");
    }


    /**
     * 会员返利
     * @param orderMaster
     */
    public void memberRebate(String token,OrderMaster orderMaster){
        Integer productCount = baseService.getTotalProductCount(orderMaster.getId().intValue());
        //会员邀请及普通消费返利
        if(StringUtils.isBlank(orderMaster.getComboLevel())){
            InviteRebateVo inviteRebateVo = HttpClientUtil.inviteRebate(orderMaster.getMemberId(), orderMaster.getSystemOrderNo(), token,productCount);
            logger.info("会员邀请及普通消费返利【【【【" + orderMaster.getSystemOrderNo() + "】】】】,操作结束" + "【【【【" + inviteRebateVo + "】】】");
        }

        //  用户冻结积分，冻结余额操作
        String prefix_key = "freezeAssetIds_" + orderMaster.getSystemOrderNo();
        String inviteResult = (String) redisService.get(prefix_key);
        logger.info("【【【【" + prefix_key + "】】】】积分操作开始");
        if (StringUtils.isNotBlank(inviteResult)) {
            JSONObject jsonObject = JSON.parseObject(inviteResult);
            Date time = new Date();
            LocalDateTime localDateTime = time.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime localExpireTime = localDateTime.plusDays(SystemConfig.ORDER_CONFIRM_TIME);
            Long total = localExpireTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
            jsonObject.put("time", total.toString());
            redisService.set(prefix_key, jsonObject.toJSONString());
        }
        logger.info("【【【【" + orderMaster.getSystemOrderNo() + "】】】】,积分操作结束" + "【【【【" + prefix_key + "】】】");
    }

    /**
     * 取消订单
     * 1. 检测当前订单是否能够取消；
     * 2. 设置订单取消状态；
     * 3. 商品货品库存恢复；
     * @param id 订单id
     * @return 取消订单操作结果
     */
    @PostMapping("/cancel/{id}")
    public JsonResult<IPage> cancel(@PathVariable("id") Long id) {
        if (id == null) {
            return result.error("参数错误");
        }

        OrderMaster orderMaster = baseService.getById(id);
        if (orderMaster == null) {
            return result.error("订单不存在");
        }
        //判断订单状态是否为待付款
        if (orderMaster.getOrderStatus() - SystemConfig.ORDER_STATUS_WAIT_PAY != 0) {
            return result.error("订单状态不对");
        }

        // 设置订单已取消状态
        OrderMaster upOrderCancel = new OrderMaster();
        upOrderCancel.setId(orderMaster.getId());
        upOrderCancel.setOrderStatus(SystemConfig.ORDER_STATUS_CANCLE);
        upOrderCancel.setCancelTime(new Date());
        if (!baseService.updateById(upOrderCancel)) {
            throw new RuntimeException("更新数据已失效");
        }

        // 商品货品数量增加
        QueryWrapper orderDetailWrapper = new QueryWrapper();
        orderDetailWrapper.eq("order_id", id);
        List<OrderDetail> orderDetails = orderDetailService.list(orderDetailWrapper);

        //商品数量/库存减少
        List<SkuAddStockVo> skuAddStockVos = new ArrayList<>();
        for (OrderDetail orderGoods : orderDetails) {
            SkuAddStockVo skuAddStockVo = new SkuAddStockVo();
            skuAddStockVo.setCount(orderGoods.getGoodsCount());
            skuAddStockVo.setSkuId(orderGoods.getSkuId());
            skuAddStockVos.add(skuAddStockVo);
        }
        int addSubStock = baseService.addAndSubSkuStock(skuAddStockVos, true,false,true);
        if (addSubStock - 0 == 0) {
            return result.error("扣库存失败");
        }

        return result.success("修改成功");
    }

    /**
     * 微信申请退款回调接口
     * @return 操作结果
     */
    @PostMapping("refund-notify")
    public Object refundNotify(@RequestBody RefundNotifyVo refundNotifyVo) {
        logger.info("微信申请退款回调接口回调结果===refundNotifyVo>" + refundNotifyVo);

        String orderSn = refundNotifyVo.getOut_trade_no();

        QueryWrapper<OrderMaster> orderQuery = new QueryWrapper<>();
        orderQuery.eq("system_order_no", orderSn).eq("hidden", 0).last("LIMIT 1");
        OrderMaster order = baseService.getOne(orderQuery);
        if (order == null) {
            return result.error("订单不存在 sn=" + orderSn);
        }

        // 检查这个订单是否已经处理过
        if (order.getOrderStatus() - SystemConfig.ORDER_STATUS_REFUNDED == 0) {
            return result.success("订单已经处理成功!");
        }

        // 设置订单 设置退还完成 ------>  支付成功，设置成 待发货
        OrderMaster upOrderReceived = new OrderMaster();
        upOrderReceived.setId(order.getId());
        upOrderReceived.setOrderStatus(SystemConfig.ORDER_STATUS_REFUNDED);
        upOrderReceived.setRefundFinishTime(new Date());
        if (!baseService.updateById(upOrderReceived)) {
            throw new RuntimeException("更新数据已失效");
        }
        return result.success("处理成功!");
    }


    /**
     * 余额支付回调
     * @return 操作结果
     */
    @PostMapping("balance-notify")
    public Object balanceNotify(@RequestBody BalanceRefundListVo list) {
        logger.info("微信申请退款回调接口回调结果===余额支付回调>" + list);
        List<BalanceRefundVo> allOrderMaster = list.getBalanceBatch();
        for(BalanceRefundVo grouponOrderMaster : allOrderMaster){
            OrderMaster order = baseService.getById(grouponOrderMaster.getOrderId());
            if (order == null) {
                return result.error("订单不存在 sn=");
            }

            // 检查这个订单是否已经处理过
            if (order.getOrderStatus() - SystemConfig.ORDER_STATUS_REFUNDED == 0) {
                return result.success("订单已经处理成功!");
            }

            //判断是否拆单
            LambdaQueryWrapper<OrderDetail> orderDetailLambdaQueryWrapper = new LambdaQueryWrapper<>();
            orderDetailLambdaQueryWrapper.eq(OrderDetail::getOrderId,order.getId());
            List<OrderDetail> orderDetails = orderDetailService.list(orderDetailLambdaQueryWrapper);
            if(orderDetails.size() - 1 > 0 && StringUtils.isNotBlank(orderDetails.get(0).getClearingInfo())){
                logger.info("订单号：【【【"+order.getSystemOrderNo()+"】】】订单拆单多份不修改状态");
                continue;
            }

            // 设置订单 设置退还完成 ------>  支付成功，设置成 待发货
            OrderMaster upOrderReceived = new OrderMaster();
            upOrderReceived.setId(order.getId());
            upOrderReceived.setOrderStatus(SystemConfig.ORDER_STATUS_REFUNDED);
            upOrderReceived.setRefundFinishTime(new Date());
            if (!baseService.updateById(upOrderReceived)) {
                throw new RuntimeException("更新数据已失效");
            }
        }
        return result.success("处理成功!");
    }



    /**
     * 微信付款成功或失败回调接口
     * @return 操作结果
     */
    @PostMapping("pay-notify")
    public Object payNotify(@RequestBody PayNotifyVo payNotifyVo,String token) {
        logger.info("回调结果===>" + payNotifyVo);
        if (!"success".equals(payNotifyVo.getResult())) {
            return result.error("支付失败");
        }

        String orderSn = payNotifyVo.getOut_trade_no();
        String outTradeNo = payNotifyVo.getTransaction_id();


        QueryWrapper<OrderMaster> orderQuery = new QueryWrapper<>();
        orderQuery.eq("system_order_no", orderSn).eq("hidden", 0).last("LIMIT 1");
        OrderMaster order = baseService.getOne(orderQuery);
        if (order == null) {
            return result.error("订单不存在 sn=" + orderSn);
        }

        // 检查这个订单是否已经处理过
        if (order.getOrderStatus() - SystemConfig.ORDER_STATUS_WAIT_PAY != 0) {
            return result.success("订单已经处理成功!");
        }

        // 设置订单 待收货 ------>  支付成功，设置成 待发货
        OrderMaster upOrderReceived = new OrderMaster();
        upOrderReceived.setId(order.getId());
        upOrderReceived.setOrderStatus(SystemConfig.ORDER_STATUS_DELIVER);
        upOrderReceived.setTransactionOrderNo(outTradeNo);
        upOrderReceived.setPayTime(new Date());
        if (!baseService.updateById(upOrderReceived)) {
            throw new RuntimeException("更新数据已失效");
        }

        //修改销量
        // 商品货品数量增加
        QueryWrapper orderDetailWrapper = new QueryWrapper();
        orderDetailWrapper.eq("order_id",order.getId());
        List<OrderDetail> orderDetails = orderDetailService.list(orderDetailWrapper);
        //商品数量/库存减少
        List<SkuAddStockVo> skuAddStockVos = new ArrayList<>();
        for(OrderDetail orderGoods : orderDetails){
            SkuAddStockVo skuAddStockVo = new SkuAddStockVo();
            skuAddStockVo.setCount(orderGoods.getGoodsCount());
            skuAddStockVo.setSkuId(orderGoods.getSkuId());
            skuAddStockVos.add(skuAddStockVo);
        }
        int addSubStock = baseService.addAndSubSkuStock(skuAddStockVos,false,true,false);
        if(addSubStock - 0 == 0){
            throw new RuntimeException("【【【【" + order.getSystemOrderNo() + "】】】】销量增加失败");
        }

        if (StringUtils.isNotBlank(order.getRemark())) {
            //调用绑定接口
            int i = HttpClientUtil.agentShareBind(order.getMemberId(), order.getRemark());
            logger.info("代理商绑定结果: [[[["+ i +"]]]]----【【【【" + order.getSystemOrderNo() + "】】】】,用户ID:" + "【【【【" + order.getMemberId() + "】】】,分享人分享码:【【【" + order.getRemark() + "】】】");
        }

        // 根据 是否拼团，处理拼团业务
        if(order.getGrouponActivityId() != null && order.getGrouponActivityId() - 0 != 0){
            grouponOrderMasterService.doGrouponService(order.getId(),order.getMemberId());
        }

        //返利
        memberRebate(token,order);

        // 取消订单超时未支付任务
        taskService.removeTask(new OrderUnpaidTask(order.getId()));
        return result.success("处理成功!");
    }


    /**
     * 按orderSn查询OrderDetail对象（orderSn）
     * @param orderSn 订单号
     * @return
     */
    @GetMapping("getOrderDetailByOrderId/{id}")
    public JsonResult getOrderDetailByOrderId(@PathVariable("id") String orderSn) {
        QueryWrapper<OrderMaster> OrderMasterWrapper = new QueryWrapper();
        OrderMasterWrapper.eq("system_order_no", orderSn).eq("hidden", 0).last("LIMIT 1");
        OrderMaster orderMaster = baseService.getOne(OrderMasterWrapper);
        if (orderMaster == null) {
            return result.error("订单不存在");
        }
        QueryWrapper orderDetailWrapper = new QueryWrapper();
        orderDetailWrapper.eq("order_id", orderMaster.getId());
        return result.success(orderDetailService.list(orderDetailWrapper));
    }

    /**
     * 用户id查询OrderMaster信息（memberId）
     * @param userId 用户id
     * @return
     */
    @GetMapping("getOrderByUserId/{id}")
    public JsonResult getOrderByUserId(@PathVariable("id") Integer userId) {
        QueryWrapper<OrderMaster> orderDetailWrapper = new QueryWrapper();
        orderDetailWrapper.eq("member_id", userId).eq("hidden", 0);
        return result.success(baseService.list(orderDetailWrapper));
    }


    /**
     * 判断该订单有没有支付（）（订单号, 套餐唯一标识）
     * @param orderSn  订单号
     * @param sign 套餐唯一标识
     * @return
     */
    @GetMapping("isBuyPackage")
    public JsonResult isBuyPackage(String orderSn, String sign) {
        QueryWrapper<OrderMaster> orderMasterQueryWrapper = new QueryWrapper();
        orderMasterQueryWrapper.eq("system_order_no", orderSn).eq("hidden", 0).last("LIMIT 1");
        OrderMaster orderMaster = baseService.getOne(orderMasterQueryWrapper);
        if (orderMaster == null) {
            return result.error("订单不存在", false);
        }
        if (orderMaster.getOrderStatus() - SystemConfig.ORDER_STATUS_WAIT_PAY == 0) {
            return result.error("订单没有支付", false);
        } else if (orderMaster.getOrderStatus() - SystemConfig.ORDER_STATUS_CANCLE == 0) {
            return result.error("订单已取消", false);
        } else {
            //判断订单详情sku数量是否为1
            QueryWrapper<OrderDetail> orderDetailQueryWrapper = new QueryWrapper();
            orderDetailQueryWrapper.eq("order_id", orderMaster.getId()).last("LIMIT 1");
            OrderDetail orderDetail = orderDetailService.getOne(orderDetailQueryWrapper);
            if (orderDetail == null) {
                return result.error("订单错误", false);
            } else {
                if (orderDetail.getGoodsCount() - 1 == 0) {
                    return result.success(orderDetail.getGoodsPrice() + "", HttpClientUtil.isPackage(orderDetail.getSkuId(), sign));
                }
            }
            return result.success("", false);
        }
    }

    /**
     * 查看物流
     * @param orderId
     * @return
     */
    @GetMapping("getTracking/{id}")
    public JsonResult isBuyPackage(@PathVariable("id") Integer orderId) {
        OrderMaster orderMaster = baseService.getById(orderId);
        if (orderMaster == null || orderMaster.getTrackingCompanyId() == null || StringUtils.isBlank(orderMaster.getTrackingNumber())) {
            return result.error("物流信息为空");
        }

        Tracking tracking = trackingService.getById(orderMaster.getTrackingCompanyId());
        if (tracking == null) {
            return result.error("不支持该物流公司");
        }

        String resultTracking = new SynQueryDemo().synQueryData(tracking.getTrackingCode(), orderMaster.getTrackingNumber(), "", "", "");
        if (StringUtils.isNotBlank(resultTracking)) {
            try {
                Logistics logistics = JSON.parseObject(resultTracking, Logistics.class);
                if (logistics != null && logistics.getStatus() - 200 == 0) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("trackingName", tracking.getTrackingCompanyName());
                    map.put("trackingNum", orderMaster.getTrackingNumber());
                    map.put("trackList", logistics.getData());
                    return result.success(map);
                } else {
                    if (logistics == null) {
                        return result.error("物流信息为空");
                    }
                    return result.error(logistics.getMessage());
                }
            } catch (Exception e) {
                return result.error("获取物流失败");
            }
        } else {
            return result.error("获取物流失败");
        }
    }
}