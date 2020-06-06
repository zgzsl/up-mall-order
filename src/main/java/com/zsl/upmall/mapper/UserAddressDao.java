/**
 * @filename:UserAddressDao 2020年04月08日
 * @project up-mall板根商城  V1.0
 * Copyright(c) 2020 binggleWang Co. Ltd. 
 * All right reserved. 
 */
package com.zsl.upmall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsl.upmall.vo.in.AddressInfo;
import org.apache.ibatis.annotations.Mapper;
import com.zsl.upmall.entity.UserAddress;
import org.apache.ibatis.annotations.Param;

/**   
 * @Description:TODO(用户和代理商数据访问层)
 *
 * @version: V1.0
 * @author: binggleWang
 * 
 */
@Mapper
public interface UserAddressDao extends BaseMapper<UserAddress> {
    AddressInfo addressInfo(@Param("id") Long addressId);
}
