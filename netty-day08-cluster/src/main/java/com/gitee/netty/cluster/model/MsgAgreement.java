package com.gitee.netty.cluster.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息协议
 *
 * @author jie
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MsgAgreement {
    /**
     * 发送至channelId
     */
    private String toChannelId;
    /**
     * 消息内容
     */
    private String content;
}
