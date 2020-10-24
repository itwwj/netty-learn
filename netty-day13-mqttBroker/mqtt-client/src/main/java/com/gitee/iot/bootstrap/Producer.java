package com.gitee.iot.bootstrap;


import com.gitee.iot.auto.MqttListener;
import com.gitee.iot.bootstrap.bean.SubMessage;
import com.gitee.iot.config.ConnectOptions;
import io.netty.channel.Channel;

import java.util.List;

/**
 * <p>生产者
 * @author jie
 */
@SuppressWarnings("ALL")
public interface Producer {

    Channel getChannel();

    Producer connect(ConnectOptions connectOptions);

    void close();

    void setMqttListener(MqttListener mqttListener);

    void pub(String topic, String message, boolean retained, int qos);

    void pub(String topic, String message);

    void pub(String topic, String message, int qos);

    void pub(String topic, String message, boolean retained);

    void sub(SubMessage... subMessages);

    void unsub(List<String> topics);

    void unsub();

    void disConnect();
}
