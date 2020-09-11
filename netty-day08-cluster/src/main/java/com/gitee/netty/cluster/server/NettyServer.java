package com.gitee.netty.cluster.server;

import com.gitee.netty.cluster.utils.ExtServerService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;




/**
 * @author jie
 */
@Slf4j
@Data
@Component
public class NettyServer implements CommandLineRunner {

    private final EventLoopGroup parentGroup = new NioEventLoopGroup();
    private final EventLoopGroup childGroup = new NioEventLoopGroup();
    private Channel channel;

    @Value("${netty.port}")
    private int port;

    @Autowired
    private ExtServerService extServerService;

    @Override
    public void run(String... args) throws Exception {
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(parentGroup, childGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new MyChannelInitializer(extServerService));
            ChannelFuture channelFuture = b.bind(port).syncUninterruptibly();
            this.channel = channelFuture.channel();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }


    public void destroy() {
        if (null == channel) {
            return;
        }
        channel.close();
        parentGroup.shutdownGracefully();
        childGroup.shutdownGracefully();
    }
}
