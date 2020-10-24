package com.gitee.websocket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 设备消息
 * @author jie
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceMsg {
    /**
     * 1请求设备信息，2发送消息
     */
    private int type;
    /**
     * 消息
     */
    private String msgInfo;
}
