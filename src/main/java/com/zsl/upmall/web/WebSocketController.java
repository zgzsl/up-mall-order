package com.zsl.upmall.web;

import com.alibaba.fastjson.JSON;
import com.zsl.upmall.aid.JsonResult;
import com.zsl.upmall.config.WebSocket;
import com.zsl.upmall.service.GrouponOrderMasterService;
import com.zsl.upmall.vo.SendMsgVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName WebSocketController
 * @Description 消息通知
 * @Author binggleW
 * @Date 2019-12-30 16:33
 * @Version 1.0
 **/
@RestController
@RequestMapping("test")
public class WebSocketController {

    @Autowired
    private GrouponOrderMasterService grouponOrderMasterService;

    protected JsonResult result = new JsonResult();

    @GetMapping("send")
    public JsonResult sendMessage(Long orderId){
        //webosocket通知
        SendMsgVo sendMsgVo =  grouponOrderMasterService.sendMsg(orderId);
        WebSocket.sendMessageAll(JSON.toJSONString(sendMsgVo));
        return result.success(JSON.toJSONString(sendMsgVo));
    }
}
