package com.github.codec.client;

import com.github.codec.proto.Person;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;

import java.nio.charset.Charset;

/**
 *
 * MyChannelInitializer的主要目的是为程序员提供了一个简单的工具，用于在某个Channel注册到EventLoop后，对这个Channel执行一些初始
 * 化操作。ChannelInitializer虽然会在一开始会被注册到Channel相关的pipeline里，但是在初始化完成之后，ChannelInitializer会将自己
 * 从pipeline中移除，不会影响后续的操作。
 * @author jie
 */
public class MyClientChannelInitializer extends ChannelInitializer<SocketChannel> {
    /**
     * 这个方法在Channel被注册到EventLoop的时候会被调用
     * @param socketChannel
     * @throws Exception
     */
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        System.out.println("=========连接到服务端=========");
        System.out.println("channelId："+socketChannel.id());

        //protobuf 处理
        socketChannel.pipeline().addLast(new ProtobufVarint32FrameDecoder());
        socketChannel.pipeline().addLast(new ProtobufDecoder(Person.getDefaultInstance()));
        socketChannel.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
        socketChannel.pipeline().addLast(new ProtobufEncoder());

        // 基于换行符号
        socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));
        // 基于指定字符串"$"
        //socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,  Unpooled.copiedBuffer("$".getBytes()));
        // 基于最大长度
        // socketChannel.pipeline().addLast(new FixedLengthFrameDecoder(4));
        // 解码转String，注意调整自己的编码格式GBK、UTF-8
        socketChannel.pipeline().addLast(new StringDecoder(Charset.forName("utf-8")));
        socketChannel.pipeline().addLast(new MyClientHandler());
    }
}
