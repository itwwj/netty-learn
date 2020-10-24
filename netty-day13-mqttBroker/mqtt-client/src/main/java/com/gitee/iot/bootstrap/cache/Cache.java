package com.gitee.iot.bootstrap.cache;


import com.gitee.iot.bootstrap.bean.SendMqttMessage;

import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>缓存
 * @author jie
 */
public class Cache {
    private static ConcurrentHashMap<Integer, SendMqttMessage> message = new ConcurrentHashMap<>();


    public static  boolean put(Integer messageId,SendMqttMessage mqttMessage){

        return message.put(messageId,mqttMessage)==null;

    }

    public static SendMqttMessage get(Integer messageId){

        return  message.get(messageId);

    }


    public static SendMqttMessage del(Integer messageId){
        return message.remove(messageId);
    }
}
