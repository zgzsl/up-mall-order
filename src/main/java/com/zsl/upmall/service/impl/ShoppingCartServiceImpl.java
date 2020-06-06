/**
 * @filename:ShoppingCartServiceImpl 2020年04月08日
 * @project up-mall板根商城  V1.0
 * Copyright(c) 2018 binggleWang Co. Ltd. 
 * All right reserved. 
 */
package com.zsl.upmall.service.impl;

import com.zsl.upmall.entity.ShoppingCart;
import com.zsl.upmall.mapper.ShoppingCartDao;
import com.zsl.upmall.service.ShoppingCartService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**   
 * @Description:TODO(用户和代理商服务实现)
 *
 * @version: V1.0
 * @author: binggleWang
 * 
 */
@Service
public class ShoppingCartServiceImpl  extends ServiceImpl<ShoppingCartDao, ShoppingCart> implements ShoppingCartService  {
	
}