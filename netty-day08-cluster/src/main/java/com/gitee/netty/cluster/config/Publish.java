package com.gitee.netty.cluster.config;

import com.gitee.netty.cluster.model.MsgAgreement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


/**
 * 发布redis消息
 * @author jie
 */
@Service
public class Publish {
    @Autowired
    private RedisTemplate<String, Object> redisMessageTemplate;

    public void pushMessage(String topic, MsgAgreement message) {
        redisMessageTemplate.convertAndSend(topic, message);
    }
}
