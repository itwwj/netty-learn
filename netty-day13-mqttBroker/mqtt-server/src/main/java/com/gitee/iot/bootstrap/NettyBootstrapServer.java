package com.gitee.iot.bootstrap;


import com.gitee.iot.config.InitBean;
import com.gitee.iot.util.IpUtils;
import com.gitee.iot.util.RemotingUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>netty 服务启动类
 * @author jie
 */
@Data
@Slf4j
public class NettyBootstrapServer extends AbstractBootstrapServer {


    private InitBean serverBean;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workGroup;
    // 启动辅助类
    ServerBootstrap bootstrap = null;

    /**
     * 服务开启
     */
    @Override
    public void start() {
        initEventPool();
        bootstrap.group(bossGroup, workGroup)
                .channel(useEpoll() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                //允许重复使用本地地址和端口
                .option(ChannelOption.SO_REUSEADDR, serverBean.isReuseaddr())
                //标识当服务器请求处理线程全满时，用于临时存放已完成三次握手的请求的队列的最大长度。如果未设置或所设置的值小于1，Java将使用默认值50
                .option(ChannelOption.SO_BACKLOG, serverBean.getBacklog())
                //ByteBuf的分配器(重用缓冲区)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                //操作接收缓冲区和发送缓冲区的大小，接收缓冲区用于保存网络协议站内收到的数据，直到应用程序读取成功，发送缓冲区用于保存发送数据，直到发送成功。
                .option(ChannelOption.SO_RCVBUF, serverBean.getRevbuf())
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        initHandler(ch.pipeline(), serverBean);
                    }
                })
                //将小的数据包组装为更大的帧进行发送 发送数据延迟
                .childOption(ChannelOption.TCP_NODELAY, serverBean.isTcpNodelay())
                //连接保活，默认值为False。启用该功能时，TCP会主动探测空闲连接的有效性。可以将此功能视为TCP的心跳机制，需要注意的是：默认的心跳间隔是7200s即2小时。Netty默认关闭该功能
                .childOption(ChannelOption.SO_KEEPALIVE, serverBean.isKeepalive())
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        bootstrap.bind(serverBean.getPort()).addListener((ChannelFutureListener) channelFuture -> {
            if (channelFuture.isSuccess())
            { log.info("服务端启动成功【" + IpUtils.intranetIp() + ":" + serverBean.getPort() + "】");}
            else
            { log.info("服务端启动失败【" + IpUtils.intranetIp() + ":" + serverBean.getPort() + "】");}
        });
    }
    /**
     * 关闭资源
     */
    @Override
    public void shutdown() {
        if (workGroup != null && bossGroup != null) {
            try {
                bossGroup.shutdownGracefully().sync();// 优雅关闭
                workGroup.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                log.info("服务端关闭资源失败【" + IpUtils.intranetIp() + ":" + serverBean.getPort() + "】");
            }
        }
    }

    @Override
    public void setServerBean(InitBean serverBean) {
        this.serverBean=serverBean;
    }





    /**
     * 初始化EnentPool 参数
     */
    private void initEventPool() {
        bootstrap = new ServerBootstrap();
        if (useEpoll()) {
            bossGroup = new EpollEventLoopGroup(serverBean.getBossThread(), new ThreadFactory() {
                private AtomicInteger index = new AtomicInteger(0);
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "LINUX_BOSS_" + index.incrementAndGet());
                }
            });
            workGroup = new EpollEventLoopGroup(serverBean.getWorkThread(), new ThreadFactory() {
                private AtomicInteger index = new AtomicInteger(0);
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "LINUX_WORK_" + index.incrementAndGet());
                }
            });

        } else {
            bossGroup = new NioEventLoopGroup(serverBean.getBossThread(), new ThreadFactory() {
                private AtomicInteger index = new AtomicInteger(0);
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "BOSS_" + index.incrementAndGet());
                }
            });
            workGroup = new NioEventLoopGroup(serverBean.getWorkThread(), new ThreadFactory() {
                private AtomicInteger index = new AtomicInteger(0);
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "WORK_" + index.incrementAndGet());
                }
            });
        }
    }




    /**
     * 判断操作系统
     *
     * @return
     */
    private boolean useEpoll() {
        return RemotingUtil.isLinuxPlatform()
                && Epoll.isAvailable();
    }
}
