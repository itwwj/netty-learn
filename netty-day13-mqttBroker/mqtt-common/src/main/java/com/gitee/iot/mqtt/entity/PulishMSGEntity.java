package com.gitee.iot.mqtt.entity;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * @author jie
 */
@Builder
@Data
@ToString
public class PulishMSGEntity {

    private String topic;
    private Object message;
}
