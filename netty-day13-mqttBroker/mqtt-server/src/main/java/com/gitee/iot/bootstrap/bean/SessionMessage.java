package com.gitee.iot.bootstrap.bean;

import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.Builder;
import lombok.Data;

/**
 消息状态
 */
@Builder
@Data
public class SessionMessage {

    private byte[] byteBuf;

    private MqttQoS qoS;

    private String topic;


    public String getString() {
        return new String(byteBuf);
    }
}
