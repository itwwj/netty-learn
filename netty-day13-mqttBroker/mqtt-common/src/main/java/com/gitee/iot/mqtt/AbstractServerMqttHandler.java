package com.gitee.iot.mqtt;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttConnectMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttSubscribeMessage;
import io.netty.handler.codec.mqtt.MqttUnsubscribeMessage;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * <p>抽象出服务端事件
 * @author jie
 */
public abstract class AbstractServerMqttHandler implements MqttHandlerIntf {
    /**
     * 验证
     * @param channel
     * @param mqttConnectMessage
     * @return
     */
    public abstract boolean connect(Channel channel, MqttConnectMessage mqttConnectMessage);

    /**
     * //发布消息
     * @param channel
     * @param mqttPublishMessage
     */
    public abstract  void  publish(Channel channel, MqttPublishMessage mqttPublishMessage);

    /**
     * //订阅主题
     * @param channel
     * @param mqttSubscribeMessage
     */
    public abstract void subscribe(Channel channel, MqttSubscribeMessage mqttSubscribeMessage);

    /**
     * 心跳回复
     * @param channel
     */
    public abstract void pong(Channel channel);

    /**
     * //取消订阅
     * @param channel
     * @param mqttMessage
     */
    public abstract  void unsubscribe(Channel channel, MqttUnsubscribeMessage mqttMessage);

    /**
     * //断开连接
     * @param channel
     */
    public abstract void disconnect(Channel channel);

    /**
     * 超时处理
     * @param channel
     * @param evt
     */
    @Override
    public abstract void doTimeOut(Channel channel, IdleStateEvent evt);
}
