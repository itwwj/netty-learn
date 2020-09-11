package com.gitee.netty.uploading.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Data;

/**
 * netty服务端启动类
 *
 * @author jie
 */
@Data
public class NettyServer {


    private Channel channel;

    public void init(int port) {
         EventLoopGroup boosGroup = new NioEventLoopGroup();
         EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(boosGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new MyServerChannelInitializer());
            ChannelFuture f = b.bind(port).syncUninterruptibly();
            channel = f.channel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
