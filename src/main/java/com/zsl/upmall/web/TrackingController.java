/**
 * @filename:TrackingController 2020年04月08日
 * @project up-mall板根商城  V1.0
 * Copyright(c) 2020 binggleWang Co. Ltd.
 * All right reserved.
 */
package com.zsl.upmall.web;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import com.zsl.upmall.aid.JsonResult;
import com.zsl.upmall.config.SynQueryDemo;
import com.zsl.upmall.entity.OrderDetail;
import com.zsl.upmall.entity.OrderMaster;
import com.zsl.upmall.entity.Tracking;
import com.zsl.upmall.mapper.OrderDetailDao;
import com.zsl.upmall.service.OrderDetailService;
import com.zsl.upmall.service.OrderMasterService;
import com.zsl.upmall.service.TrackingService;
import com.zsl.upmall.service.TrackingSubpackageService;
import com.zsl.upmall.vo.out.GoodsVo;
import com.zsl.upmall.vo.out.Logistics;
import com.zsl.upmall.vo.out.SubpackageVo;
import com.zsl.upmall.vo.out.TrackingVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>自动生成工具：mybatis-dsc-generator</p>
 *
 * <p>说明： API接口层</P>
 *
 * @version: V1.0
 * @author: binggleWang
 * @time 2020年04月08日
 */

@RestController
@RequestMapping("/tracking")
public class TrackingController {

    protected JsonResult result = new JsonResult();

    @Resource
    private TrackingService trackingService;

    @Resource
    private OrderDetailService orderDetailService;

    @Resource
    private OrderMasterService orderMasterService;

    @Resource
    private TrackingSubpackageService trackingSubpackageService;

    /**
     * 物流列表
     *
     * @param orderNo
     * @return
     */
    @GetMapping("/getTracking/list")
    public JsonResult orderByTrackingList(String orderNo) {
        List<TrackingVo> orderTracking = orderMasterService.getOrderTracking(orderNo);
        if (orderTracking.isEmpty()) {
            List<SubpackageVo> orderSubpackageTracking = orderMasterService.getOrderSubpackageTracking(orderNo);
            for (SubpackageVo subpackageVo : orderSubpackageTracking) {
                String[] split = subpackageVo.getSubpackage().split(",");
                List<GoodsVo> goodsList = orderDetailService.orderGoodsList(split);
                subpackageVo.setGoodsList(goodsList);
            }
            return result.success(orderSubpackageTracking);
        } else {
            return result.success(orderTracking);
        }
    }


    /**
     * 包裹物流信息
     *
     * @param trackingSn
     * @return
     */
    @GetMapping("getTracking/subpackage")
    public JsonResult isSubpackageBuyPackage(String trackingSn) {
        Tracking tracking = trackingSubpackageService.subpackageTracking(trackingSn);
        if (tracking == null) {
            tracking = orderMasterService.orderTracking(trackingSn);
        }
        String resultTracking = new SynQueryDemo().synQueryData(tracking.getTrackingCode(), trackingSn, "", "", "");
        if (StringUtils.isNotBlank(resultTracking)) {
            try {
                Logistics logistics = JSON.parseObject(resultTracking, Logistics.class);
                if (logistics != null && logistics.getStatus() - 200 == 0) {
                    Map<String, Object> map = new HashMap<>(16);
                    map.put("trackingName", tracking.getTrackingCompanyName());
                    map.put("trackingNum", trackingSn);
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