package com.github.custom.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 操作类
 *
 * @author jie
 */
public class MyServerHandler extends ChannelInboundHandlerAdapter {


    /**
     * 当客户端主动连接服务端,通道活跃后触发
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SocketChannel channel = (SocketChannel) ctx.channel();
        System.out.println("客户端："+channel.localAddress().getHostString()+"连接");

    }

    /**
     * 通道有消息触发
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.print(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())  + "接收到消息：");
        System.out.println(msg);
        ctx.writeAndFlush(msg);
    }
}
