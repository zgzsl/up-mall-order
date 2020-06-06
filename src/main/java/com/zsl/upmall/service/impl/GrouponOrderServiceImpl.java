/**
 * @filename:GrouponOrderServiceImpl 2020年04月08日
 * @project up-mall板根商城  V1.0
 * Copyright(c) 2018 binggleWang Co. Ltd. 
 * All right reserved. 
 */
package com.zsl.upmall.service.impl;

import com.zsl.upmall.entity.GrouponOrder;
import com.zsl.upmall.mapper.GrouponOrderDao;
import com.zsl.upmall.service.GrouponOrderService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**   
 * @Description:TODO(拼团主订单表服务实现)
 *
 * @version: V1.0
 * @author: binggleWang
 * 
 */
@Service
public class GrouponOrderServiceImpl  extends ServiceImpl<GrouponOrderDao, GrouponOrder> implements GrouponOrderService  {
	
}