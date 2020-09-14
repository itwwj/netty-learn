package com.gitee.netty.cluster.config;

import com.alibaba.fastjson.JSON;
import com.gitee.netty.cluster.model.MsgAgreement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;


/**
 * 发布redis消息
 *
 * @author jie
 */
@Slf4j
@Component
public class Publish {
    @Autowired
    private StringRedisTemplate redisTemplate;

    public void pushMessage(String topic, MsgAgreement message) {
        log.info("向 "+topic+"发送消息："+message);
        redisTemplate.convertAndSend(topic, JSON.toJSONString(message));
    }
}
