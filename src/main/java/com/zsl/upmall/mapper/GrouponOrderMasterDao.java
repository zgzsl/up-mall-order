/**
 * @filename:GrouponOrderMasterDao 2020年04月08日
 * @project up-mall板根商城  V1.0
 * Copyright(c) 2020 binggleWang Co. Ltd. 
 * All right reserved. 
 */
package com.zsl.upmall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zsl.upmall.vo.MiniNoticeVo;
import com.zsl.upmall.vo.SendMsgVo;
import com.zsl.upmall.vo.out.GrouponListVo;
import org.apache.ibatis.annotations.Mapper;
import com.zsl.upmall.entity.GrouponOrderMaster;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**   
 * @Description:TODO(数据访问层)
 *
 * @version: V1.0
 * @author: binggleWang
 * 
 */
@Mapper
public interface GrouponOrderMasterDao extends BaseMapper<GrouponOrderMaster> {
    IPage<GrouponListVo> getGrouponListByPage(IPage<GrouponListVo> page,@Param("grouponOrderId") Integer grouponOrderId);
    List<MiniNoticeVo> getGroupNoticeList(@Param("grouponOrderId") Long grouponOrderId,@Param("grouponStatus") Integer grouponStatus);
    SendMsgVo sendMsg(@Param("orderId") Long orderId);
}
