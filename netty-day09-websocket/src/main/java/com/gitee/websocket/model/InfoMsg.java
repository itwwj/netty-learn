package com.gitee.websocket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jie
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InfoMsg {
    /**
     * 消息传输给某个，管道ID
     */
    private String channelId;
    /**
     * 消息类型；1、Text 2、QueryInfoReq 3、Feedback
     */
    private Integer msgType;
    /**
     * 消息对象
     */
    private Object msgObj;
}
