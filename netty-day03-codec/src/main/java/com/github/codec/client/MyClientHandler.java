package com.github.codec.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 客户端事件操作类
 * @author jie
 */
public class MyClientHandler extends ChannelInboundHandlerAdapter {
    /**
     * 当客户端主动链接服务端的链接后，通道就是活跃的了此时触发
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //通知服务端链接建立成功
        String str = "与客户端链接建立成功" + " " + new Date();
        ctx.writeAndFlush(str);
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //接收msg消息
        ByteBuf buf = (ByteBuf) msg;
        byte[] msgByte = new byte[buf.readableBytes()];
        buf.readBytes(msgByte);
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 接收到消息：" );
        System.out.println(new String(msgByte, Charset.forName("utf-8")));
    }
    /**
     * 当客户端主动断开服务端的链接后，这个通道就是不活跃的。也就是说客户端与服务端的关闭了通信通道并且不可以传输数据
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("===================" + ctx.channel().localAddress().toString() + " 断开连接===================");
    }
    /**
     * 抓住异常，当发生异常的时候，可以做一些相应的处理，比如打印日志、关闭链接
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        System.out.println("发现异常：\r\n" + cause.getMessage());
    }
}
