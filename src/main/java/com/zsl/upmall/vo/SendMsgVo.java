package com.zsl.upmall.vo;

import lombok.Data;

/**
 * @ClassName SendMsgVo
 * @Description websocket消息体
 * @Author binggleW
 * @Date 2020-05-18 11:58
 * @Version 1.0
 **/
@Data
public class SendMsgVo {
    private String avatar;
    private String nickName;
    private String msgType;
}
