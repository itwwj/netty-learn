package com.gitee.netty.cluster.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户管道信息；记录某个用户分配到某个服务端
 *
 * @author jie
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceChannelInfo {
    /**
     * 服务端IP
     */
    private String ip;
    /**
     * 服务端port
     */
    private int port;
    /**
     * channelId
     */
    private String channelId;
    /**
     * 链接时间
     */
    private Date linkDate;
}
