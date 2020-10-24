package com.gitee.iot.bootstrap.handler;


import com.gitee.iot.bootstrap.ChannelService;
import com.gitee.iot.bootstrap.bean.MqttChannel;
import com.gitee.iot.config.RabbitMQConfig;
import com.gitee.iot.exception.NoFindHandlerException;
import com.gitee.iot.mqtt.AbstractMqttHander;
import com.gitee.iot.mqtt.MqttHandlerIntf;
import com.gitee.iot.mqtt.AbstractServerMqttHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>默认 mqtthandler处理
 * @author jie
 */
@ChannelHandler.Sharable
@Slf4j
@Component
public class DefaultAbstractMqttHandler extends AbstractMqttHander {


    private final MqttHandlerIntf mqttHandlerApi;

    @Autowired
    ChannelService channelService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public DefaultAbstractMqttHandler(MqttHandlerIntf mqttHandlerApi) {
        super(mqttHandlerApi);
        this.mqttHandlerApi = mqttHandlerApi;
    }

    @Override
    public void doMessage(ChannelHandlerContext channelHandlerContext, MqttMessage mqttMessage) {
        Channel channel = channelHandlerContext.channel();
        AbstractServerMqttHandler abstractServerMqttHandler;
        if (mqttHandlerApi instanceof AbstractServerMqttHandler) {
            abstractServerMqttHandler = (AbstractServerMqttHandler) mqttHandlerApi;
        } else {
            throw new NoFindHandlerException("server handler 不匹配");
        }
        MqttFixedHeader mqttFixedHeader = mqttMessage.fixedHeader();
        //连接认证 上线通知
        if (mqttFixedHeader.messageType().equals(MqttMessageType.CONNECT)) {
            if (!abstractServerMqttHandler.connect(channel, (MqttConnectMessage) mqttMessage)) {
                channel.close();
            }
           // connectMsgToServer((MqttConnectMessage) mqttMessage);
            return;
        }
        MqttChannel mqttChannel = channelService.getMqttChannel(channelService.getDeviceId(channel));
        if (mqttChannel != null && mqttChannel.isLogin()) {
            switch (mqttFixedHeader.messageType()) {
                //发布消息
                case PUBLISH:
                    abstractServerMqttHandler.publish(channel, (MqttPublishMessage) mqttMessage);
                    break;
                //订阅主题
                case SUBSCRIBE:
                    abstractServerMqttHandler.subscribe(channel, (MqttSubscribeMessage) mqttMessage);
                    break;
                //心跳请求
                case PINGREQ:
                    abstractServerMqttHandler.pong(channel);
                    break;
                //取消订阅
                case UNSUBSCRIBE:
                    abstractServerMqttHandler.unsubscribe(channel, (MqttUnsubscribeMessage) mqttMessage);
                    break;
                //断开连接
                case DISCONNECT:
                    abstractServerMqttHandler.disconnect(channel);
                    break;
                //发布确认
                case PUBACK:
                    mqttHandlerApi.puback(channel, mqttMessage);
                    break;
                //发布收到(QOS 2第一步)
                case PUBREC:
                    mqttHandlerApi.pubrec(channel, mqttMessage);
                    break;
                //发布释放(QOS 2第二步)
                case PUBREL:
                    mqttHandlerApi.pubrel(channel, mqttMessage);
                    break;
                //发布完成发布释放(QOS 2第三步)
                case PUBCOMP:
                    mqttHandlerApi.pubcomp(channel, mqttMessage);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 向管理服务器发送设备登录消息
     * @param mqttMessage
     */

    private void connectMsgToServer(MqttConnectMessage mqttMessage){
        rabbitTemplate.convertAndSend(RabbitMQConfig.NETTY_Connect_WORKQUEUES,mqttMessage.payload().clientIdentifier());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("【DefaultMqttHandler：channelActive】" + ctx.channel().remoteAddress().toString() + "链接成功");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("exception", cause);
        mqttHandlerApi.close(ctx.channel());
    }
}
