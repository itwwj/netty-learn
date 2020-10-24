package com.gitee.websocket.server.socket;

import com.gitee.websocket.model.Device;
import com.gitee.websocket.utils.CacheUtil;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.websocketx.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.UUID;


/**
 * @author jie
 */
@Data
@Slf4j
public class MyServerHandler extends ChannelInboundHandlerAdapter {


    private WebSocketServerHandshaker webSocketServerHandshaker;

    /**
     * 当客户端主动链接服务端的链接后，这个通道就是活跃的了。也就是客户端与服务端建立了通信通道并且可以传输数据
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SocketChannel channel = (SocketChannel) ctx.channel();
        String channelId = channel.id().toString();
        log.info("有一设备链接到本服务端 channelId:" + channel.id() + " IP:" + channel.localAddress().getHostString());
        Device device = Device.builder().channelId(channelId)
                .number(UUID.randomUUID().toString())
                .ip(channel.localAddress().getHostString())
                .port(channel.remoteAddress().getPort())
                .connectTime(new Date())
                .build();
        CacheUtil.deviceGroup.put(channelId,device);
        CacheUtil.cacheClientChannel.put(channel.id().toString(), channel);
    }

    /**
     * 当客户端主动断开服务端的链接后，这个通道就是不活跃的。也就是说客户端与服务端的关闭了通信通道并且不可以传输数据
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("设备断开链接" + ctx.channel().localAddress().toString());
        String channelId = ctx.channel().id().toString();
        //移除设备信息
        CacheUtil.deviceGroup.remove(channelId);
        CacheUtil.cacheClientChannel.remove(channelId);
    }

    /**
     * 处理通道内的数据
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info(" 接收到消息：" + msg);
        CacheUtil.wsChannelGroup.writeAndFlush(new TextWebSocketFrame(msg.toString()));
    }

    /**
     * 发现异常关闭连接打印日志
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        String channelId = ctx.channel().id().toString();
        //移除设备信息
        CacheUtil.deviceGroup.remove(channelId);
        CacheUtil.cacheClientChannel.remove(channelId);
        ctx.close();
        log.error("异常信息：\r\n" + cause.getMessage());
    }
}





