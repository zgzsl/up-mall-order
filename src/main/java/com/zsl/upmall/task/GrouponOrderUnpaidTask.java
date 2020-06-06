package com.zsl.upmall.task;

import com.zsl.upmall.entity.GrouponActivities;
import com.zsl.upmall.service.GrouponOrderMasterService;
import com.zsl.upmall.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName GrouponOrderUnpaidTask
 * @Description 拼团结算
 * @Author binggleW
 * @Date 2020-05-15 9:27
 * @Version 1.0
 **/
public class GrouponOrderUnpaidTask extends Task{
    private final Logger logger = LoggerFactory.getLogger(GrouponOrderUnpaidTask.class);
    private long joinGroupId = -1;
    private GrouponActivities activityDetail;

    public GrouponOrderUnpaidTask(long joinGroupId,GrouponActivities activityDetail){
        super("GrouponOrderUnpaidTask-" + joinGroupId, 0);
        this.joinGroupId = joinGroupId;
        this.activityDetail = activityDetail;
    }

    public GrouponOrderUnpaidTask(long joinGroupId,GrouponActivities activityDetail,long delayInMilliseconds){
        super("GrouponOrderUnpaidTask-" + joinGroupId, delayInMilliseconds);
        this.joinGroupId = joinGroupId;
        this.activityDetail = activityDetail;
    }

    @Override
    public void run() {
        logger.info("系统开始处理延时任务---拼团时间到了，开始结算---【【【" + this.joinGroupId+"】】】");
        GrouponOrderMasterService grouponOrderMasterService = BeanUtil.getBean(GrouponOrderMasterService.class);
        grouponOrderMasterService.settlementGroup(Integer.parseInt(this.joinGroupId + "") ,this.activityDetail);
        logger.info("系统开始处理延时任务---拼团，结算 结束---【【【" + this.joinGroupId+"】】】");
    }
}
