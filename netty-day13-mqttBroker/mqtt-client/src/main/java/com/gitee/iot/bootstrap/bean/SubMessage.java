package com.gitee.iot.bootstrap.bean;

import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.Builder;
import lombok.Data;

/**
 * <p>订阅消息
 * @author jie
 */
@Builder
@Data
public class SubMessage {
    private String topic;

    private MqttQoS qos;
}
