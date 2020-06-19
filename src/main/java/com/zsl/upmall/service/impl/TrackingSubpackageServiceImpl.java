/**
 * @filename:TrackingServiceImpl 2020年04月08日
 * @project up-mall板根商城  V1.0
 * Copyright(c) 2018 binggleWang Co. Ltd.
 * All right reserved.
 */
package com.zsl.upmall.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsl.upmall.entity.Tracking;
import com.zsl.upmall.entity.TrackingSubpackage;
import com.zsl.upmall.mapper.TrackingSubpackageDao;
import com.zsl.upmall.service.TrackingSubpackageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description:TODO(服务实现)
 *
 * @version: V1.0
 * @author: binggleWang
 *
 */
@Service
public class TrackingSubpackageServiceImpl extends ServiceImpl<TrackingSubpackageDao, TrackingSubpackage> implements TrackingSubpackageService {

    @Resource
    private TrackingSubpackageDao trackingSubpackageDao;

    /**
     * 获取分包裹物流公司信息
     * @param trackingSn
     * @return
     */
    @Override
    public Tracking subpackageTracking(String trackingSn) {
        return trackingSubpackageDao.subpackageTracking(trackingSn);

    }
}