package com.gitee.netty.cluster.config;

import com.alibaba.fastjson.JSON;
import com.gitee.netty.cluster.model.MsgAgreement;
import com.gitee.netty.cluster.utils.CacheUtil;
import com.gitee.netty.cluster.utils.MsgUtil;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * @author jie
 */
@Slf4j
@Service
public class MsgAgreementReceiver implements ReceiveInterface {

    /**
     * 接收redis推送的消息如果当前服务连接的有此设备就推送消息
     * @param message 消息
     */
    @Override
    public void receiveMessage(Object message) {
        log.info("接收到redis PUSH消息：{}", message);
        MsgAgreement msgAgreement = JSON.parseObject(message.toString(), MsgAgreement.class);
        String toChannelId = msgAgreement.getToChannelId();
        Channel channel = CacheUtil.cacheChannel.get(toChannelId);
        if (null == channel) {
            return;
        }
        channel.writeAndFlush(MsgUtil.obj2Json(msgAgreement));
    }
}
