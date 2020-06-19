/**
 * @filename:TrackingDao 2020年04月08日
 * @project up-mall板根商城  V1.0
 * Copyright(c) 2020 binggleWang Co. Ltd.
 * All right reserved.
 */
package com.zsl.upmall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.zsl.upmall.entity.Tracking;
import com.zsl.upmall.entity.TrackingSubpackage;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description:TODO(数据访问层)
 * @version: V1.0
 * @author: binggleWang
 */
@Mapper
public interface TrackingSubpackageDao extends BaseMapper<TrackingSubpackage> {
    /**
     * 获取分包裹物流公司信息
     *
     * @param trackingSn
     * @return
     */
    Tracking subpackageTracking(String trackingSn);
}
