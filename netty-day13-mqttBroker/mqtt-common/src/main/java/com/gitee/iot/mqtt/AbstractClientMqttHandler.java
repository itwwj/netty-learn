package com.gitee.iot.mqtt;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttSubAckMessage;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * <p> 抽象出服务端事件
 * @author jie
 */
public abstract class AbstractClientMqttHandler implements MqttHandlerIntf {
    /**
     * 超时
     * @param channel
     * @param evt
     */
    @Override
    public void doTimeOut(Channel channel, IdleStateEvent evt) {
        heart(channel, evt);
    }

    /**
     * 心跳
     * @param channel
     * @param evt
     */
    public abstract void heart(Channel channel, IdleStateEvent evt);

    /**
     * 订阅确认
     * @param channel
     * @param mqttMessage
     */
    public abstract void suback(Channel channel, MqttSubAckMessage mqttMessage);

    /**
     * @param channel
     * @param i
     */
    public abstract void pubBackMessage(Channel channel, int i);

    /**
     * 取消订阅确认
     * @param channel
     * @param mqttMessage
     */
    public abstract void unsubBack(Channel channel, MqttMessage mqttMessage);

}
