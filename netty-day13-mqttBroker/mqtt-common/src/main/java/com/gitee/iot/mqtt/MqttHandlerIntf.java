package com.gitee.iot.mqtt;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * <p>消息处理api
 * @author jie
 */
public interface MqttHandlerIntf {
    /**
     * 关闭通道
     * @param channel
     */
    void close(Channel channel);

    /**
     * //发布确认(QOS1)
     * @param channel
     * @param mqttMessage
     */
    void puback(Channel channel, MqttMessage mqttMessage);

    /**
     * //发布收到(QOS2 第一步)
     * @param channel
     * @param mqttMessage
     */
    void pubrec(Channel channel, MqttMessage mqttMessage);

    /**
     * //发布释放(QOS2 第二部)
     * @param channel
     * @param mqttMessage
     */
    void pubrel(Channel channel, MqttMessage mqttMessage);

    /**
     * //发布完成(QOS2,第三部)
     * @param channel
     * @param mqttMessage
     */
    void pubcomp(Channel channel, MqttMessage mqttMessage);

    /**
     * 超时处理
     * @param channel
     * @param evt
     */
    void doTimeOut(Channel channel, IdleStateEvent evt);
}
