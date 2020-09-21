package com.gitee.netty.cluster.utils;

import com.gitee.netty.cluster.config.MsgPub;
import com.gitee.netty.cluster.config.RedisUtil;
import com.gitee.netty.cluster.model.DeviceChannelInfo;
import com.gitee.netty.cluster.model.MsgAgreement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author jie
 */
@Service
public class CacheService {

    @Autowired
    private MsgPub msgPub;
    @Autowired
    private RedisUtil redisUtil;


    public void push(MsgAgreement msgAgreement) {
        DeviceChannelInfo deviceChannelInfo = redisUtil.selectByChannel(msgAgreement.getToChannelId());
        if (deviceChannelInfo == null) {
            return;
        }
        msgPub.pushMessage("message_pub"+deviceChannelInfo.getIp()+deviceChannelInfo.getPort(), msgAgreement);
    }

    public RedisUtil getRedisUtil() {
        return redisUtil;
    }
}
