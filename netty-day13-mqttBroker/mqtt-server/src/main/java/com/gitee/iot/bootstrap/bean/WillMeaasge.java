package com.gitee.iot.bootstrap.bean;

import lombok.Builder;
import lombok.Data;

/**
 * <p>遗嘱消息
 * @author jie
 */
@Builder
@Data
public class WillMeaasge {

    private String willTopic;

    private String willMessage;

    private boolean isRetain;

    private int qos;
}
