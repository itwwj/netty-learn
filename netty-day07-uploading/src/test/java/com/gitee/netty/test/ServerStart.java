package com.gitee.netty.test;

import com.gitee.netty.uploading.server.NettyServer;

/**
 * @author jie
 */
public class ServerStart {
    public static void main(String[] args) {
        new NettyServer().init(1100);
    }
}
