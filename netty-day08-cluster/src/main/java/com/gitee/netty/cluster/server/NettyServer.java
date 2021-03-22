package com.gitee.netty.cluster.server;

import com.alibaba.fastjson.JSON;
import com.gitee.netty.cluster.model.ServerInfo;
import com.gitee.netty.cluster.utils.CacheUtil;
import com.gitee.netty.cluster.utils.CacheService;
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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.concurrent.TimeUnit;


/**
 * netty服务端启动类
 *
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
    @Value("${netty.ip}")
    private String ip;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private CacheService cacheService;

    @Override
    public void run(String... args) throws Exception {
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(parentGroup, childGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childHandler(new MyChannelInitializer(cacheService));
            ChannelFuture channelFuture = b.bind(new InetSocketAddress(ip,port)).syncUninterruptibly();
            this.channel = channelFuture.channel();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        String ip = InetAddress.getLocalHost().getHostAddress();
        Date date = new Date();
        //每3秒向注册中心注册一下自己的服务端信息 如果5秒没有注册redis便清除此服务端信息
        CacheUtil.executorService.submit(() -> {
            try {
                while (channel.isActive()) {
                    redisTemplate.opsForValue().set("nettyServer" + ip, JSON.toJSONString(new ServerInfo(ip, 1100, date)), 5 * 1000, TimeUnit.MILLISECONDS);
                    Thread.sleep(3*1000);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        });
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
