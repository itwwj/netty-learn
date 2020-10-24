package com.gitee.iot.auto;

import io.netty.handler.codec.mqtt.MqttQoS;

import java.lang.annotation.*;

/**
 * @author jie
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MqttMessageListener {


    String[] topic();
    //默认QOS为Q0
    MqttQoS qos() default MqttQoS.AT_MOST_ONCE;
}
