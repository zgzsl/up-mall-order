/**
 * @filename:UserAddressService 2020年04月08日
 * @project up-mall板根商城  V1.0
 * Copyright(c) 2020 binggleWang Co. Ltd. 
 * All right reserved. 
 */
package com.zsl.upmall.service;

import com.zsl.upmall.entity.UserAddress;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zsl.upmall.vo.in.AddressInfo;

/**
 * @Description:TODO(用户和代理商服务层)
 * @version: V1.0
 * @author: binggleWang
 * 
 */
public interface UserAddressService extends IService<UserAddress> {
    AddressInfo addressInfo(Long addressId);
}