package com.gitee.netty.cluster.utils;

import com.gitee.netty.cluster.config.MsgPub;
import com.gitee.netty.cluster.config.RedisUtil;
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

    public void push(MsgAgreement msgAgreement){
        msgPub.pushMessage("message_pub", msgAgreement);
    }

    public RedisUtil getRedisUtil() {
        return redisUtil;
    }
}
