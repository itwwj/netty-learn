package com.gitee.netty.cluster.config;

import com.alibaba.fastjson.JSON;
import com.gitee.netty.cluster.model.MsgAgreement;
import com.gitee.netty.cluster.utils.CacheUtil;
import com.gitee.netty.cluster.utils.MsgUtil;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;


/**
 * redis订阅消息处理实现类
 *
 * @author jie
 */
@Slf4j
@Component
public class MsgReceiver extends MessageListenerAdapter {

    @Autowired
    private StringRedisTemplate redisTemplate;
    /**
     * 接收redis推送的消息如果当前服务连接的有此设备就推送消息
     *
     * @param message 消息
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String msg = redisTemplate.getStringSerializer().deserialize(message.getBody());
        String topic = redisTemplate.getStringSerializer().deserialize(message.getChannel());
        log.info("来自" + topic + "的消息：" + msg);

        MsgAgreement msgAgreement = JSON.parseObject(msg, MsgAgreement.class);
        String toChannelId = msgAgreement.getToChannelId();
        Channel channel = CacheUtil.cacheChannel.get(toChannelId);
        if (null == channel) {
            return;
        }
        channel.writeAndFlush(MsgUtil.obj2Json(msgAgreement));
    }
}
