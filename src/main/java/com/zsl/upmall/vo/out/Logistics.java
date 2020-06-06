package com.zsl.upmall.vo.out;

import java.util.List;

import lombok.Data;

@Data
public class Logistics {
	
	private String message;//消息体
	
	private String nu;//快递单号
	
	private Integer ischeck;//是否签收标记
	
	private String com;//快递公司编码
	
	private Integer status;//通讯状态
	
	private Integer state;//快递单当前状态，包括0在途，1揽收，2疑难，3签收，4退签，5派件，6退回等7个状态
	
	private List<DateInfo> data;

}

@Data
class DateInfo{
	
	private String ftime;//格式化后时间
	
	private String context;//物流轨迹节点内容
}
