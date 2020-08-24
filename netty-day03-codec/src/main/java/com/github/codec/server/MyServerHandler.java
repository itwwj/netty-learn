package com.github.codec.server;

import com.github.codec.proto.Person;
import com.googlecode.protobuf.format.JsonFormat;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;
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
        Person.Builder builder = Person.newBuilder();
        builder.setName("server");
        builder.setAge(10);
        ctx.writeAndFlush(builder.build());
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
        System.out.println( JsonFormat.printToString((Person) msg));
    }

    /**
     * 当客户端主动断开连接,通道不活跃触发
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("===================客户端:" + ctx.channel().localAddress().toString() + " 断开连接===================");
    }

    /**
     * 当连接发生异常时触发
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //在发生异常时主动关掉连接
        ctx.close();
        System.out.println("发现异常：\r\n" + cause.getMessage());
    }
}
