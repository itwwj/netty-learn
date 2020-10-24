package com.gitee.iot.bootstrap;


import com.gitee.iot.bootstrap.bean.MqttChannel;
import com.gitee.iot.bootstrap.bean.WillMeaasge;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttConnectMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;

import java.util.List;
import java.util.Set;

/**
 * <p>消息处理
 * @author jie
 */
public interface ChannelService {
    /**
     * 获取mqttchannel
     *
     * @param deviceId
     * @return
     */
    MqttChannel getMqttChannel(String deviceId);

    boolean connectSuccess(String s, MqttChannel build);

    void suscribeSuccess(String deviceId, Set<String> topics);

    void loginSuccess(Channel channel, String deviceId, MqttConnectMessage mqttConnectMessage);

    void publishSuccess(Channel channel, MqttPublishMessage mqttPublishMessage);

    void closeSuccess(String deviceId, boolean isDisconnect);

    void sendWillMsg(WillMeaasge willMeaasge);

    String getDeviceId(Channel channel);

    void unsubscribe(String deviceId, List<String> topics1);

    void doPubrel(Channel channel, int mqttMessage);

    void doPubrec(Channel channel, int mqttMessage);
}
