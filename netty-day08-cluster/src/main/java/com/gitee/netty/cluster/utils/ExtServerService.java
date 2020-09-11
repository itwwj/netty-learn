package com.gitee.netty.cluster.utils;

import com.gitee.netty.cluster.config.Publish;
import com.gitee.netty.cluster.config.RedisUtil;
import com.gitee.netty.cluster.model.MsgAgreement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



/**
 * @author jie
 */
@Service
public class ExtServerService {

    @Autowired
    private Publish publish;
    @Autowired
    private RedisUtil redisUtil;

    public void push(MsgAgreement msgAgreement){
        publish.pushMessage("message_pub", msgAgreement);
    }

    public RedisUtil getRedisUtil() {
        return redisUtil;
    }
}
