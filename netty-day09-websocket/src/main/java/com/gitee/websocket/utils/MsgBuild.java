package com.gitee.websocket.utils;

import com.alibaba.fastjson.JSON;
import com.gitee.websocket.model.InfoMsg;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * @author jie
 */
public class MsgBuild {


    public static String buildMsg(InfoMsg infoProtocol) {
        return JSON.toJSONString(infoProtocol) + "\r\n";
    }

    public static InfoMsg getMsg(String str) {
        return JSON.parseObject(str, InfoMsg.class);
    }

    public static TextWebSocketFrame buildWsMsgText(String channelId, String msgInfo) {
        InfoMsg info = new InfoMsg();
        info.setChannelId(channelId);
        info.setMsgType(1);
        info.setMsgObj(msgInfo);
        return new TextWebSocketFrame(JSON.toJSONString(info));
    }
}
