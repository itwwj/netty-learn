package com.gitee.netty.cluster.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * 服务端信息
 *
 * @author jie
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerInfo {
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
