package com.gitee.iot.bootstrap.bean;

import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.Builder;
import lombok.Data;

/**
 * <p>保留消息
 * @author jie
 */
@Data
@Builder
public class RetainMessage {

    private byte[] byteBuf;
    private MqttQoS qoS;

    public String getString() {
        return new String(byteBuf);
    }
}
