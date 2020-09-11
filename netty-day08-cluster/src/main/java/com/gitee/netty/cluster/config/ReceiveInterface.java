package com.gitee.netty.cluster.config;


/**
 * @author jie
 */
public interface ReceiveInterface {
    /**
     * 接收redis推送的消息
     *
     * @param message 消息
     */
      void receiveMessage(Object message);

}
