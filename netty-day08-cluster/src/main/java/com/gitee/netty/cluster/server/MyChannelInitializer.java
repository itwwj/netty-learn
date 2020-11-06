package com.gitee.netty.cluster.server;

import com.gitee.netty.cluster.utils.CacheService;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;


/**
 *
 *  MyChannelInitializer的主要目的是为程序员提供了一个简单的工具，用于在某个Channel注册到EventLoop后，对这个Channel执行一些初始
 * 化操作。ChannelInitializer虽然会在一开始会被注册到Channel相关的pipeline里，但是在初始化完成之后，ChannelInitializer会将自己
 * 从pipeline中移除，不会影响后续的操作。
 * @author jie
 */
public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {

    private CacheService cacheService;

    public MyChannelInitializer(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Override
    protected void initChannel(SocketChannel channel) {
        /**
         * 心跳监测
         * 1、readerIdleTimeSeconds 读超时时间
         * 2、writerIdleTimeSeconds 写超时时间
         * 3、allIdleTimeSeconds    读写超时时间
         * 4、TimeUnit.SECONDS 秒[默认为秒，可以指定]
         */
        channel.pipeline().addLast(new IdleStateHandler(60, 0, 0));
        // 基于换行符号
        channel.pipeline().addLast(new LineBasedFrameDecoder(1024));
        // 解码转String，注意调整自己的编码格式GBK、UTF-8
        channel.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));
        // 解码转String，注意调整自己的编码格式GBK、UTF-8
        channel.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
        // 在管道中添加我们自己的接收数据实现方法
        channel.pipeline().addLast(new MyServerHandler(cacheService));
    }
}
