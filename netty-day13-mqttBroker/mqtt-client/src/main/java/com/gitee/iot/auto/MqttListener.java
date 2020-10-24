package com.gitee.iot.auto;

/**
 * <p>订阅topic[]监听接口
 * @author jie
 */
public interface MqttListener {

    /**
     * 回掉函数
     * @param topic
     * @param msg
     */
    void callBack(String topic, String msg);

    /**
     *错误回掉
     * @param e
     */
    void callThrowable(Throwable e);
}
