package com.gitee.netty.boot.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * netty服务端启动类
 *
 * @author jie
 */
@Data
@Slf4j
@Component
public class NettyServer {


    private EventLoopGroup boosGroup = new NioEventLoopGroup();
    private EventLoopGroup workerGroup = new NioEventLoopGroup();
    private Channel channel;


    public ChannelFuture init(int port) {
        ChannelFuture f=null;
        try {
            //用于启动NIO服务端的辅助启动类,目的是降低服务端的开发复杂度
            ServerBootstrap b = new ServerBootstrap();
            b.group(boosGroup, workerGroup)
                    //对应JDK NIO类库中的ServerSocketChannel
                    .channel(NioServerSocketChannel.class)
                    //配置NioServerSocketChannel的TCP参数
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    //绑定I/O的事件处理类
                    .childHandler(new MyServerChannelInitializer());
            //调用它的bind操作监听端口号,调用同步阻塞方法sync等待绑定操作完成
            f = b.bind(port).sync();
            channel = f.channel();
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
        return f;
    }

    /**
     * 关闭
     */
    public void close() {
        if (null == channel) {
            return;
        }
        channel.close();
        boosGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

}
