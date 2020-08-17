package com.github.introduction.client;

import com.github.introduction.server.MyServerChannelInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * netty客户端启动类
 *
 * @author jie
 */
public class NettyClient {


    private static String ip = "127.0.0.1";
    private static int port = 1100;

    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).
                    channel(NioSocketChannel.class).
                    option(ChannelOption.AUTO_READ, true).
                    handler(new MyClientChannelInitializer());
            ChannelFuture future = b.connect(ip, port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
