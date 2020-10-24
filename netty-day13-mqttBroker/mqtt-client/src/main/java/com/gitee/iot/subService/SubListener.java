package com.gitee.iot.subService;


import com.alibaba.fastjson.JSON;
import com.gitee.iot.auto.MqttListener;
import com.gitee.iot.auto.MqttMessageListener;
import com.gitee.iot.config.RabbitMQConfig;
import com.gitee.iot.mqtt.entity.PulishMSGEntity;
import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>自动配置类
 * @author jie
 */
@Slf4j
@Service
@MqttMessageListener(qos = MqttQoS.AT_LEAST_ONCE, topic = {"/2222"})
public class SubListener implements MqttListener {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void callBack(String topic, String msg) {
        log.info("topic:" + topic);
        log.info("message:" + msg);
        PulishMSGEntity build = PulishMSGEntity.builder().topic(topic)
                .message(msg)
                .build();
        rabbitTemplate.convertAndSend(RabbitMQConfig.NETTY_Publish_WORKQUEUES, JSON.toJSONString(build));
    }

    @Override
    public void callThrowable(Throwable e) {

    }
}
