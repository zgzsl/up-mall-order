/**
 * @filename:TrackingService 2020年04月08日
 * @project up-mall板根商城  V1.0
 * Copyright(c) 2020 binggleWang Co. Ltd. 
 * All right reserved. 
 */
package com.zsl.upmall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zsl.upmall.entity.Tracking;
import com.zsl.upmall.entity.TrackingSubpackage;


/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: binggleWang
 * 
 */
public interface TrackingSubpackageService extends IService<TrackingSubpackage> {


    /**
     * 获取分包裹物流公司信息
     *
     * @param trackingSn
     * @return
     */
    Tracking subpackageTracking(String trackingSn);
}