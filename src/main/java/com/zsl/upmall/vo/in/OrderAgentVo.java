package com.zsl.upmall.vo.in;

import com.zsl.upmall.entity.OrderDetail;
import com.zsl.upmall.entity.OrderMaster;
import lombok.Data;

import java.util.List;

/**
 * @Author: CYW
 * @Description:
 * @Date:Created ON 2020/7/10 14:23
 */
@Data
public class OrderAgentVo {

    private OrderMaster orderMaster;

    private List<OrderDetail> orderDetailList;
}