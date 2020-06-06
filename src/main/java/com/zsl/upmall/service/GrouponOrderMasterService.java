/**
 * @filename:GrouponOrderMasterService 2020年04月08日
 * @project up-mall板根商城  V1.0
 * Copyright(c) 2020 binggleWang Co. Ltd. 
 * All right reserved. 
 */
package com.zsl.upmall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zsl.upmall.entity.GrouponActivities;
import com.zsl.upmall.entity.GrouponOrderMaster;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zsl.upmall.vo.MiniNoticeVo;
import com.zsl.upmall.vo.SendMsgVo;
import com.zsl.upmall.vo.out.GrouponListVo;

import java.util.List;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: binggleWang
 * 
 */
public interface GrouponOrderMasterService extends IService<GrouponOrderMaster> {
    /**
     * 拼团列表
     * @param page
     * @param grouponOrderId
     * @return
     */
    IPage<GrouponListVo> getGrouponListByPage(IPage<GrouponListVo> page,Integer grouponOrderId);

    void settlementGroup(Integer joinGroupId,GrouponActivities activityDetail);

    /**
     *  场景说明抽奖结果通知
     * @param openId
     * @param pages 点击跳转页面
     * @param goodName 详细内容商品名称
     * @param orderSn 订单编号
     * @param totalFee 支付金额
     */
    void push(String openId,String pages,String goodName, String orderSn,String totalFee);

    /**
     * 场景说明拼团失败通知
     * @param openId
     * @param pages
     * @param goodName  详细内容商品名称
     * @param pinPrice 拼团价
     * @param refundFee 退款金额
     * @param notice 温馨提示
     */
    void push1(String openId,String pages,String goodName, String pinPrice,String refundFee,String notice);

    /**
     * 场景说明拼团成功通知
     * @param openId
     * @param pages
     * @param goodName 详细内容商品名称
     * @param pinPrice 拼团价格
     * @param notice 温馨提示
     */
    void push2(String openId,String pages,String goodName,String pinPrice,String notice);

    /**
     * 拼团业务处理
     */
    void doGrouponService(Long orderId,Integer userId);

    List<MiniNoticeVo> getGroupNoticeList(Long grouponOrderId,Integer grouponStatus);

    SendMsgVo sendMsg(Long orderId);
}