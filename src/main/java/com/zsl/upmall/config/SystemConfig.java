package com.zsl.upmall.config;

/**
 * 系统设置
 */
public class SystemConfig {
    // 域名 (测试)
    //public static String TEST_HOST = "http://upmall-beta.cntracechain.com";
    public static String TEST_HOST = "http://upmall.cntracechain.com";
    // 支付域名 (测试)
    //public static String TEST_PAY_HOST = "http://zs-beta.cntracechain.com";
    public static String TEST_PAY_HOST = "https://mall.cntracechain.com";
    // 活动信息ZSET 前缀
    public final static String ACTIVE_INFO_PREFIX = "ACTIVE_";
    // 团队信息HASH 前缀
    public final static String GROUP_INFO_PREFIX = "GROUPON_";
    // 未中奖，抽奖列表
    public final static String NOT_WIN_LIST_PREFIX = "NOT_WIN_";
    //参团凭证，redis前缀
    public final static String GROUP_PREFIX = "JOIN_GROUP_";
    // 参团凭证，基数
    public final static  Integer BASE_VOUCHER = 800000;
    //微信支付
    public final static Integer WEIXIN_PAY = 2;
    //余额支付
    public final static Integer BALANCE_PAY = 3;
    //确认收货30天(积分，余额)
    public final static Integer ORDER_CONFIRM_TIME = 30;
    // 订单超时取消 （毫秒）
    public final static Integer ORDER_UNPAID = 30 * 60 * 1000;
    // 订单状态（ 待付款）
    public final static Integer ORDER_STATUS_WAIT_PAY = 0;
    // 订单状态（ 待收货）
    public final static Integer ORDER_STATUS_RECIEVE = 1;
    // 订单状态（ 已完成）
    public final static Integer ORDER_STATUS_FINISH = 2;
    // 订单状态（ 已取消）
    public final static Integer ORDER_STATUS_CANCLE = 3;
    // 订单状态 （待发货）
    public final static Integer ORDER_STATUS_DELIVER = 4;
    // 订单状态（退款中）
    public final static Integer ORDER_STATUS_REFUNDINGD = 5;
    // 订单状态 （退款完成）
    public final static Integer ORDER_STATUS_REFUNDED = 6;

    //sku 详情接口路径 (sql代替)
    public  static String SKU_DETAIL_URL = TEST_HOST + "/common-goods/common/skuDetail";
    // 扣减库存  (sql代替)
    public  static String SKU_SUB_ADD_STOCK = TEST_HOST + "/common-goods/common/skuSell";
    // 获取 SKU用户售价  (sql代替)
    public  static String SKU_USER_COST_PRICE = TEST_HOST + " /common-goods/upMall/userPrice/sku";
    // 获取地址详情  (sql代替)
    public static String ADDRESS_DETAIL = TEST_HOST + "/user/userAddress/getById";
   //将地址设置成假删除，并且新增一条  (sql代替)
   public static String ADDRESS_DELETE_ADD = TEST_HOST + "/user/userAddress/setDeleteAndAdd";



   //代理商绑定地址
    public static String AGENT_SHARE_BIND = TEST_HOST + "/user/userMember/updateRelation";
   //会员邀请及普通消费返利
   public static String MEMBER_INVITEREBATE_URL = TEST_HOST + "/up_mall_rebate/rebate/inviteRebate";
    //套餐判断
    public static String IS_ORDER_PACKAGE = TEST_HOST + "/common-goods/upMall/uncalibrated/customizedCheck";
    //微信退款地址
    public static String WEIXIN_REFUND_URL = TEST_PAY_HOST + "/baseService/wxpay/refund";
    //微信退款回调地址
    public static String REFUND_NOTIFY_URL = TEST_HOST + "/order/order/refund-notify";
    // 余额退款回调地址
    public static String BALANCE_NOTIFY_URL = TEST_HOST + "/order/order/balance-notify";
    //微信统一下单接口
    public static String WEIXIN_UNION_RUL = TEST_PAY_HOST + "/baseService/wxpay/unifiedOrder";
    //微信支付回调地址
    public static String BUSINESS_NOTIFY_URL = TEST_HOST + "/order/order/pay-notify";
    //系统标识
    public final static String SYSTEM_UNIQUE_CODE = "up-mall";
    // 扣减用户积分和余额
    public static String DEDUCT_USER_BALANCE = TEST_HOST + "/user/userMember/updateUserBalanceOrIntegral";
    // 批量 添加和扣减用户余额
    public static String DEDUCT_USER_BALANCE_BATCH = TEST_HOST + "/user/userMember/updateBatchUserBalance";
    // 添加用户余额扣减记录
    public static  String DEDUCT_USER_BALANCE_LOG = TEST_HOST + "/upmall-point/balance/insert";
    //sku 拼团价格
    public static String  SKU_GROUP_PRICE = TEST_HOST + "/common-goods/upMall/groupon/skuPrice/";


    public static String getStatusText(Integer status){
        if(status == null){
            return "";
        }
        if(status - ORDER_STATUS_WAIT_PAY == 0){
            return "待付款";
        }else if(status - ORDER_STATUS_RECIEVE == 0){
            return "待收货";
        }else if(status - ORDER_STATUS_FINISH == 0){
            return "已完成";
        }else if(status - ORDER_STATUS_CANCLE == 0){
            return "已取消";
        }else if(status - ORDER_STATUS_DELIVER == 0){
            return "待发货";
        }else if(status - ORDER_STATUS_REFUNDINGD == 0){
            return "退款中";
        }else if(status - ORDER_STATUS_REFUNDED == 0){
            return "退款完成";
        }else {
            return "";
        }
    }

}