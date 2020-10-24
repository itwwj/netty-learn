package com.gitee.iot.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

/**
 * @author jie
 */
public class RabbitMQConfig {

    public static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    public static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    public static final String EXCHANGE_TOPICS_INFORM="exchange_topics_inform";
    /**
     * 设备登录消息转发队列
     */
    public static final String NETTY_Connect_WORKQUEUES="netty_Connect_WorkQueues";
    /**
     * 设备登录消息转发队列
     */
    public static final String NETTY_Publish_WORKQUEUES="netty_Publish_WorkQueues";
    /**
     * 设备登出消息转发队列
     */
    public static final String NETTY_Exit_WORKQUEUES="netty_Exit_WorkQueues";


    /**
     * 交换机配置
     * ExchangeBuilder提供了fanout、direct、topic、header交换机类型的配置
     * @return the exchange
     */
    @Bean(EXCHANGE_TOPICS_INFORM)
    public Exchange EXCHANGE_TOPICS_INFORM() {
        //durable(true)持久化，消息队列重启后交换机仍然存在
        return ExchangeBuilder.topicExchange(EXCHANGE_TOPICS_INFORM).durable(true).build();
    }

    /**
     * 声明队列
     * @return
     */
    @Bean(QUEUE_INFORM_SMS)
    public Queue QUEUE_INFORM_SMS() {
        Queue queue = new Queue(QUEUE_INFORM_SMS);
        return queue;
    }

    /**
     * 声明队列
     * @return
     */
    @Bean(QUEUE_INFORM_EMAIL)
    public Queue QUEUE_INFORM_EMAIL() {
        Queue queue = new Queue(QUEUE_INFORM_EMAIL);
        return queue;
    }


    /** channel.queueBind(INFORM_QUEUE_SMS,"inform_exchange_topic","inform.#.sms.#");
     * 绑定队列到交换机 .
     *
     * @param queue    the queue
     * @param exchange the exchange
     * @return the binding
     */
    @Bean
    public Binding BINDING_QUEUE_INFORM_SMS(@Qualifier(QUEUE_INFORM_SMS) Queue queue,
                                            @Qualifier(EXCHANGE_TOPICS_INFORM) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("inform.#.sms.#").noargs();
    }
    @Bean
    public Binding BINDING_QUEUE_INFORM_EMAIL(@Qualifier(QUEUE_INFORM_EMAIL) Queue queue,
                                              @Qualifier(EXCHANGE_TOPICS_INFORM) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("inform.#.email.#").noargs();
    }

    /**
     * 工作模式登录登出
     * @return
     */
    @Bean
    public Queue connect() {
        return new Queue(NETTY_Connect_WORKQUEUES,true);
    }

    /**
     * 工作模式设备发布消息
     * @return
     */
    @Bean
    public Queue publish() {
        return new Queue(NETTY_Publish_WORKQUEUES,true);
    }

    /**
     * 工作模式设备上报状态
     * @return
     */
    @Bean
    public Queue createWorkQueuesOnQueue() {
        return new Queue(NETTY_Exit_WORKQUEUES,true);
    }
}
