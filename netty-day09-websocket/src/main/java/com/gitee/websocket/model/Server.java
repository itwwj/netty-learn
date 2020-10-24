package com.gitee.websocket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 服务端信息
 * @author jie
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Server {
    /**
     * 服务类型
     */
    private String typeInfo;
    /**
     * IP
     */
    private String ip;
    /**
     * 端口
     */
    private int port;
    /**
     * 启动时间
     */
    private Date openDate;
}
