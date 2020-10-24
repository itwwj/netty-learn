package com.gitee.iot.bootstrap.queue;

import com.lmax.disruptor.RingBuffer;

/**
 * @author jie
 */
public interface MessageStarter<T> {
    RingBuffer<T> getRingBuffer();

    void shutdown();
}
