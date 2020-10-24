package com.gitee.iot.bootstrap.queue;


import com.gitee.iot.bootstrap.bean.SendMqttMessage;
import lombok.Data;

/**
 * @author jie
 */
@Data
public class MessageEvent {
    private SendMqttMessage message;
}
