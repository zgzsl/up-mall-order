package com.zsl.upmall.task;

import com.zsl.upmall.service.GrouponOrderMasterService;
import com.zsl.upmall.util.BeanUtil;
import com.zsl.upmall.util.CharUtil;
import com.zsl.upmall.vo.GroupOrderStatusEnum;
import com.zsl.upmall.vo.MiniNoticeVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @ClassName GroupNoticeUnpaidTask
 * @Description 拼团通知
 * @Author binggleW
 * @Date 2020-05-16 9:53
 * @Version 1.0
 **/
public class GroupNoticeUnpaidTask extends Task {
    private final Logger logger = LoggerFactory.getLogger(GroupNoticeUnpaidTask.class);

    private long joinGroupId = -1;
    private Integer status;
    private Integer type;


    public GroupNoticeUnpaidTask(long joinGroupId, long delayInMilliseconds,Integer status,Integer type){
        super("GroupNoticeUnpaidTask-" + joinGroupId, delayInMilliseconds);
        this.joinGroupId = joinGroupId;
        this.status = status;
        this.type = type;
    }


    @Override
    public void run() {
        logger.info("拼团结果通知开始---【【【" + this.joinGroupId+"】】】");
        GrouponOrderMasterService grouponOrderMasterService = BeanUtil.getBean(GrouponOrderMasterService.class);
        //场景说明抽奖结果通知
        if(this.type - 0 == 0){
            List<MiniNoticeVo> miniNoticeVos = grouponOrderMasterService.getGroupNoticeList(this.joinGroupId,GroupOrderStatusEnum.SUCCESS.getCode());
            for(MiniNoticeVo miniNoticeVo : miniNoticeVos){
                grouponOrderMasterService.push(miniNoticeVo.getOpenId(),"pages/collageOrderList/collageOrderList",CharUtil.getString15Length(miniNoticeVo.getGoodsName()+miniNoticeVo.getGoodsSpc()),miniNoticeVo.getOrderSn(),miniNoticeVo.getTotalFee()+"");
            }
        }else if(this.type - 1 == 0){
            //场景说明拼团失败通知
            List<MiniNoticeVo> miniNoticeVos = grouponOrderMasterService.getGroupNoticeList(this.joinGroupId,GroupOrderStatusEnum.FAILED.getCode());
            for(MiniNoticeVo miniNoticeVo : miniNoticeVos){
                grouponOrderMasterService.push1(miniNoticeVo.getOpenId(),"pages/collageOrderList/collageOrderList",CharUtil.getString15Length(miniNoticeVo.getGoodsName()+miniNoticeVo.getGoodsSpc()),miniNoticeVo.getGoodsPrice()+"",miniNoticeVo.getTotalFee()+"","拼团失败");
            }
        }else if(this.type - 2 == 0){
            //场景说明拼团成功通知
            List<MiniNoticeVo> miniNoticeVos = grouponOrderMasterService.getGroupNoticeList(this.joinGroupId,GroupOrderStatusEnum.SUCCESS.getCode());
            List<MiniNoticeVo> miniNoticeVosFail = grouponOrderMasterService.getGroupNoticeList(this.joinGroupId,GroupOrderStatusEnum.FAILED.getCode());
            miniNoticeVos.addAll(miniNoticeVosFail);
            for(MiniNoticeVo miniNoticeVo : miniNoticeVos){
                String notice = "";
                if(miniNoticeVo.getActivityMode() - 0 == 0){
                    notice = "拼团成功";
                }else if(miniNoticeVo.getActivityMode() - 1 == 0){
                    notice = "成团但没拼中，退款并获得随机奖励金";
                }
                grouponOrderMasterService.push2(miniNoticeVo.getOpenId(),"pages/collageOrderList/collageOrderList",CharUtil.getString15Length(miniNoticeVo.getGoodsName()+miniNoticeVo.getGoodsSpc()),miniNoticeVo.getGoodsPrice()+"",notice);
            }
        }
        logger.info("拼团结果通知开始---【【【" + this.joinGroupId+"】】】");
    }
}
