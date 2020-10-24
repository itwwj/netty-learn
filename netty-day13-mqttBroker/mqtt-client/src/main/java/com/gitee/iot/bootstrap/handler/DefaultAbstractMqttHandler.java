package com.gitee.iot.bootstrap.handler;


import com.gitee.iot.auto.MqttListener;
import com.gitee.iot.bootstrap.MqttProducer;
import com.gitee.iot.bootstrap.Producer;
import com.gitee.iot.config.ConnectOptions;
import com.gitee.iot.mqtt.AbstractClientMqttHandler;
import com.gitee.iot.mqtt.AbstractMqttHander;
import com.gitee.iot.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jie
 */
@ChannelHandler.Sharable
@Slf4j
public class DefaultAbstractMqttHandler extends AbstractMqttHander {
    private AbstractClientMqttHandler mqttHandlerApi;

    private MqttProducer mqttProducer;

    private MqttListener mqttListener;

    private ConnectOptions connectOptions;

    public DefaultAbstractMqttHandler(ConnectOptions connectOptions, AbstractClientMqttHandler mqttHandlerApi, Producer producer, MqttListener mqttListener) {
        super(mqttHandlerApi);
        this.mqttProducer =(MqttProducer) producer;
        this.mqttListener = mqttListener;
        this.mqttHandlerApi=mqttHandlerApi;
        this.connectOptions=connectOptions;
    }



    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        ConnectOptions.MqttOpntions mqtt = connectOptions.getMqtt();
        log.info("【DefaultMqttHandler：channelActive】"+ctx.channel().localAddress().toString()+"启动成功");
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.CONNECT,false, MqttQoS.AT_LEAST_ONCE,false,10);
        MqttConnectVariableHeader mqttConnectVariableHeader = new MqttConnectVariableHeader(MqttVersion.MQTT_3_1_1.protocolName(),MqttVersion.MQTT_3_1_1.protocolLevel(),mqtt.isHasUserName(),mqtt.isHasPassword(),mqtt.isHasWillRetain(),mqtt.getWillQos(),mqtt.isHasWillFlag(),mqtt.isHasCleanSession(),mqtt.getKeepAliveTime());
        MqttConnectPayload mqttConnectPayload = new MqttConnectPayload(mqtt.getClientIdentifier(),mqtt.getWillTopic(),mqtt.getWillMessage(),mqtt.getUserName(),mqtt.getPassword());
        MqttConnectMessage mqttSubscribeMessage = new MqttConnectMessage(mqttFixedHeader,mqttConnectVariableHeader,mqttConnectPayload);
        channel.writeAndFlush(mqttSubscribeMessage);
    }



    @Override
    public void doMessage(ChannelHandlerContext channelHandlerContext, MqttMessage mqttMessage) {
        MqttFixedHeader mqttFixedHeader = mqttMessage.fixedHeader();
        switch (mqttFixedHeader.messageType()){
            case UNSUBACK:
                mqttHandlerApi.unsubBack(channelHandlerContext.channel(),mqttMessage);
                break;
            case CONNACK:
                mqttProducer.connectBack((MqttConnAckMessage) mqttMessage);
                break;
            case PUBLISH:
                publish(channelHandlerContext.channel(),(MqttPublishMessage)mqttMessage);
                break;
            case PUBACK:
                mqttHandlerApi.puback(channelHandlerContext.channel(),mqttMessage);
                break;
            case PUBREC:
                mqttHandlerApi.pubrec(channelHandlerContext.channel(),mqttMessage);
                break;
            case PUBREL:
                mqttHandlerApi.pubrel(channelHandlerContext.channel(),mqttMessage);
                break;
            case PUBCOMP:
                mqttHandlerApi.pubcomp(channelHandlerContext.channel(),mqttMessage);
                break;
            case SUBACK:
                mqttHandlerApi.suback(channelHandlerContext.channel(),(MqttSubAckMessage)mqttMessage);
                break;
            default:
                break;
        }
    }

    private void publish(Channel channel,MqttPublishMessage mqttMessage) {
        MqttFixedHeader mqttFixedHeader = mqttMessage.fixedHeader();
        MqttPublishVariableHeader mqttPublishVariableHeader = mqttMessage.variableHeader();
        ByteBuf payload = mqttMessage.payload();
        byte[] bytes = ByteBufUtil.copyByteBuf(payload); //
        if(mqttListener!=null){
            mqttListener.callBack(mqttPublishVariableHeader.topicName(),new String(bytes));
        }
        switch (mqttFixedHeader.qosLevel()){
            case AT_MOST_ONCE:
                break;
            case AT_LEAST_ONCE:
                mqttHandlerApi.pubBackMessage(channel,mqttPublishVariableHeader.messageId());
                break;
            case EXACTLY_ONCE:
                mqttProducer.pubRecMessage(channel,mqttPublishVariableHeader.messageId());
                break;
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        mqttProducer.getNettyBootstrapClient().doubleConnect();
        if(mqttListener!=null){
            mqttListener.callThrowable(cause);
        }
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        mqttProducer.getNettyBootstrapClient().doubleConnect();
    }
}
