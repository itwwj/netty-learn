package com.gitee.netty.boot.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * MyChannelInitializer的主要目的是为程序员提供了一个简单的工具，用于在某个Channel注册到EventLoop后，对这个Channel执行一些初始
 * 化操作。ChannelInitializer虽然会在一开始会被注册到Channel相关的pipeline里，但是在初始化完成之后，ChannelInitializer会将自己
 * 从pipeline中移除，不会影响后续的操作。
 *
 * @author jie
 */
@Slf4j
public class MyServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    /**
     * 这个方法在Channel被注册到EventLoop的时候会被调用
     *
     * @param socketChannel
     * @throws Exception
     */
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        log.info("=========有客户端连接服务器=========");
        log.info("ip:" + socketChannel.localAddress().getHostString() + "         port:" + socketChannel.localAddress().getPort());
        // 基于换行符号
        socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));
        // 解码转String，注意调整自己的编码格式GBK、UTF-8
        socketChannel.pipeline().addLast(new StringDecoder(Charset.forName("utf8")));
        // 解码转String，注意调整自己的编码格式GBK、UTF-8
        socketChannel.pipeline().addLast(new StringEncoder(Charset.forName("utf8")));
        socketChannel.pipeline().addLast(new MyServerHandler());

    }
}
