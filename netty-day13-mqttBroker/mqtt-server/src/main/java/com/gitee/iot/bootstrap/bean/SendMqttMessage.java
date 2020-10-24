package com.gitee.iot.bootstrap.bean;

import com.gitee.iot.enums.ConfirmStatus;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.Builder;
import lombok.Data;

/**
 * <p> mqtt 消息
 * @author jie
 */
@Builder
@Data
public class SendMqttMessage {
    private int messageId;

    private Channel channel;
    /**
     * 消息状态
     */
    private volatile ConfirmStatus confirmStatus;

    private long time;

    private byte[]  byteBuf;

    private boolean isRetain;

    private MqttQoS qos;

    private String topic;
}
