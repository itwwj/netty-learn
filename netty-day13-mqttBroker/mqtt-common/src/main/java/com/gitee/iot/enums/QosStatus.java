package com.gitee.iot.enums;

/**
 *      QOS消息质量
 * @author jie
 */
public enum QosStatus {
    /**
     * 已发送 没收到RECD （发送）
     */
    PUBD,  
    /**
     * publish 推送回复过（发送）
     */
    RECD, 
}
