package com.gitee.netty.cluster.config;

import com.gitee.netty.cluster.utils.NetWorkUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.net.UnknownHostException;

/**
 * 用户管道信息；记录某个用户分配到某个服务端
 * @author jie
 */
@Configuration
public class SubConfig {

    @Value("${netty.port}")
    private int port;

    /**
     * 接受广播消息配置 接受主题格式为：message_pub+本机ip+本程序端口
     * @param connectionFactory
     * @param msgAgreementListenerAdapter
     * @return
     * @throws UnknownHostException
     */
    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory, MessageListenerAdapter msgAgreementListenerAdapter) throws UnknownHostException {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(msgAgreementListenerAdapter, new PatternTopic("message_pub"+ NetWorkUtils.getHost()+port));
        return container;
    }
}
