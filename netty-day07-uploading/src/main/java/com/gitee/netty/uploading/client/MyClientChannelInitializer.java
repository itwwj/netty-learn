package com.gitee.netty.uploading.client;

import com.gitee.netty.uploading.codec.CustomDecoder;
import com.gitee.netty.uploading.codec.CustomEncoder;
import com.gitee.netty.uploading.model.FileTransferProtocol;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

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
        //对象传输处理
        socketChannel.pipeline().addLast(new CustomDecoder(FileTransferProtocol.class));
        socketChannel.pipeline().addLast(new CustomEncoder(FileTransferProtocol.class));
        socketChannel.pipeline().addLast(new MyClientHandler());
    }
}
