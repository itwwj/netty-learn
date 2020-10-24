package com.gitee.iot.bootstrap;

import io.netty.channel.Channel;

/**
 * @author jie
 */
@SuppressWarnings("ALL")
public interface BootstrapClient {
    void shutdown();

    void initEventPool();

    Channel start();
}
