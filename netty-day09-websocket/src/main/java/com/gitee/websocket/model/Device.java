package com.gitee.websocket.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 设备信息
 * @author jie
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Device {
    /**
     * 通信管道ID
     */
    private String channelId;
    /**
     * 设备编号
     */
    private String number;
    /**
     * 设备IP
     */
    private String ip;
    /**
     * 设备端口
     */
    private int port;
    /**
     * 连接时间
     */
    private Date connectTime;
}
