package com.gitee.iot.bootstrap.channel;


import com.gitee.iot.bootstrap.cache.Cache;
import com.gitee.iot.enums.ConfirmStatus;
import com.gitee.iot.mqtt.AbstractClientMqttHandler;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.concurrent.ScheduledFuture;

/**
 * @author jie
 */
@Slf4j
public class MqttHandlerServiceAbstract extends AbstractClientMqttHandler {

    @Override
    public void close(Channel channel) {}

    @Override
    public void puback(Channel channel, MqttMessage mqttMessage) {
        MqttMessageIdVariableHeader messageIdVariableHeader = (MqttMessageIdVariableHeader) mqttMessage.variableHeader();
        int messageId = messageIdVariableHeader.messageId();
        Optional.ofNullable(Cache.del(messageId)).ifPresent(sendMqttMessage -> {
            sendMqttMessage.setConfirmStatus(ConfirmStatus.COMPLETE);
        });
    }

    @Override
    public void pubrec(Channel channel, MqttMessage mqttMessage ) {
        MqttMessageIdVariableHeader messageIdVariableHeader = (MqttMessageIdVariableHeader) mqttMessage.variableHeader();
        int messageId = messageIdVariableHeader.messageId();
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.PUBREL,false, MqttQoS.AT_LEAST_ONCE,false,0x02);
        MqttMessageIdVariableHeader from = MqttMessageIdVariableHeader.from(messageId);
        MqttMessage mqttPubAckMessage = new MqttMessage(mqttFixedHeader,from);
        Optional.ofNullable(Cache.get(messageId)).ifPresent(sendMqttMessage -> {
            sendMqttMessage.setTimestamp(System.currentTimeMillis());
            sendMqttMessage.setConfirmStatus(ConfirmStatus.PUBREL);
        });
        channel.writeAndFlush(mqttPubAckMessage);
    }

    @Override
    public void pubrel(Channel channel, MqttMessage mqttMessage ) {
        MqttMessageIdVariableHeader messageIdVariableHeader = (MqttMessageIdVariableHeader) mqttMessage.variableHeader();
        int messageId = messageIdVariableHeader.messageId();
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.PUBCOMP,false, MqttQoS.AT_MOST_ONCE,false,0x02);
        MqttMessageIdVariableHeader from = MqttMessageIdVariableHeader.from(messageId);
        MqttMessage mqttPubAckMessage = new MqttMessage(mqttFixedHeader,from);
        Optional.ofNullable(Cache.del(messageId)).ifPresent(sendMqttMessage -> {
            sendMqttMessage.setConfirmStatus(ConfirmStatus.COMPLETE);
        });
        channel.writeAndFlush(mqttPubAckMessage);
    }

    @Override
    public void pubcomp(Channel channel,MqttMessage mqttMessage ) {
        MqttMessageIdVariableHeader messageIdVariableHeader = (MqttMessageIdVariableHeader) mqttMessage.variableHeader();
        int messageId = messageIdVariableHeader.messageId();
        Optional.ofNullable(Cache.del(messageId)).ifPresent(sendMqttMessage -> {
            sendMqttMessage.setConfirmStatus(ConfirmStatus.COMPLETE);
        });
    }

    @Override
    public void heart(Channel channel, IdleStateEvent evt) {
        MqttFixedHeader fixedHeader = new MqttFixedHeader(MqttMessageType.PINGREQ, false, MqttQoS.AT_MOST_ONCE, false, 0);
        MqttMessage mqttMessage  = new MqttMessage(fixedHeader);
        log.info("发送心跳");
        channel.writeAndFlush(mqttMessage);
    }

    @Override
    public void suback(Channel channel, MqttSubAckMessage mqttMessage) {
        ScheduledFuture<?> scheduledFuture = channel.attr(getKey(Integer.toString(mqttMessage.variableHeader().messageId()))).get();
        if(scheduledFuture!=null){
            scheduledFuture.cancel(true);
        }
    }
    //qos1 send
    @Override
    public void pubBackMessage(Channel channel, int messageId) {
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.PUBACK,false, MqttQoS.AT_LEAST_ONCE,false,0x02);
        MqttMessageIdVariableHeader from = MqttMessageIdVariableHeader.from(messageId);
        MqttPubAckMessage mqttPubAckMessage = new MqttPubAckMessage(mqttFixedHeader,from);
        channel.writeAndFlush(mqttPubAckMessage);
    }

    @Override
    public void unsubBack(Channel channel, MqttMessage mqttMessage) {
        int messageId;
        if(mqttMessage instanceof  MqttUnsubAckMessage){
            MqttUnsubAckMessage mqttUnsubAckMessage = (MqttUnsubAckMessage)mqttMessage;
            messageId= mqttUnsubAckMessage.variableHeader().messageId();
        }
        else {
            MqttMessageIdVariableHeader o =(MqttMessageIdVariableHeader) mqttMessage.variableHeader();
            messageId= o.messageId();
        }
        if(messageId>0){
            ScheduledFuture<?> scheduledFuture = channel.attr(getKey(Integer.toString(messageId))).get();
            if(!scheduledFuture.isCancelled()){
                scheduledFuture.cancel(true);
            }
        }
    }

    private AttributeKey<ScheduledFuture<?>> getKey(String id){
        return   AttributeKey.valueOf(id);
    }

}