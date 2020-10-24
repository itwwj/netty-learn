package com.gitee.iot.bootstrap.bean;


import com.gitee.iot.enums.ConfirmStatus;
import lombok.Builder;
import lombok.Data;

/**
 * <p>发送消息
 * @author jie
 */
@Data
@Builder
public class SendMqttMessage {

    private String Topic;

    private byte[] payload;

    private int qos;

    private boolean retained;

    private boolean dup;

    private int messageId;


    private long timestamp;

    private volatile ConfirmStatus confirmStatus;
}
