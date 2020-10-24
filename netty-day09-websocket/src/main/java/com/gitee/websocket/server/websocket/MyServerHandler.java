package com.gitee.websocket.server.websocket;

import com.alibaba.fastjson.JSON;
import com.gitee.websocket.model.InfoMsg;
import com.gitee.websocket.utils.CacheUtil;
import com.gitee.websocket.utils.MsgBuild;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

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
        log.info("有一客户端链接到本服务端 channelId:" + channel.id() + " IP:" + channel.localAddress().getHostString());
        CacheUtil.wsChannelGroup.add(ctx.channel());
    }

    /**
     * 当客户端主动断开服务端的链接后，这个通道就是不活跃的。也就是说客户端与服务端的关闭了通信通道并且不可以传输数据
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端断开链接" + ctx.channel().localAddress().toString());
        CacheUtil.wsChannelGroup.remove(ctx.channel());
    }

    /**
     * 处理通道内的数据
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //http
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest httpRequest = (FullHttpRequest) msg;
            if (!httpRequest.decoderResult().isSuccess()) {
                DefaultFullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
                // 返回应答给客户端
                if (httpResponse.status().code() != 200) {
                    ByteBuf buf = Unpooled.copiedBuffer(httpResponse.status().toString(), CharsetUtil.UTF_8);
                    httpResponse.content().writeBytes(buf);
                    buf.release();
                }
                // 如果是非Keep-Alive，关闭连接
                ChannelFuture f = ctx.channel().writeAndFlush(httpResponse);
                if (httpResponse.status().code() != 200) {
                    f.addListener(ChannelFutureListener.CLOSE);
                }
                return;
            }

            WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory("ws:/" + ctx.channel() + "/websocket", null, false);
            webSocketServerHandshaker = wsFactory.newHandshaker(httpRequest);
            if (null == webSocketServerHandshaker) {
                WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
            } else {
                webSocketServerHandshaker.handshake(ctx.channel(), httpRequest);
            }
            return;
        }
        //ws
        if (msg instanceof WebSocketFrame) {
            WebSocketFrame webSocketFrame = (WebSocketFrame) msg;
            //关闭请求
            if (webSocketFrame instanceof CloseWebSocketFrame) {
                webSocketServerHandshaker.close(ctx.channel(), (CloseWebSocketFrame) webSocketFrame.retain());
                return;
            }
            //ping请求
            if (webSocketFrame instanceof PingWebSocketFrame) {
                ctx.channel().write(new PongWebSocketFrame(webSocketFrame.content().retain()));
                return;
            }
            //只支持文本格式，不支持二进制消息
            if (!(webSocketFrame instanceof TextWebSocketFrame)) {
                throw new Exception("仅支持文本格式");
            }
            String request = ((TextWebSocketFrame) webSocketFrame).text();
            log.info("服务端收到：" + request);
            InfoMsg info = JSON.parseObject(request, InfoMsg.class);
            //socket转发消息
            String channelId = info.getChannelId();
            Channel channel = CacheUtil.cacheClientChannel.get(channelId);
            if (null == channel) {
                return;
            }
            channel.writeAndFlush(MsgBuild.buildMsg(info));
            //websocket消息反馈发送成功
            ctx.writeAndFlush(MsgBuild.buildWsMsgText(ctx.channel().id().toString(), "success！"));
        }
    }

    /**
     * 发现异常关闭连接打印日志
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        CacheUtil.wsChannelGroup.remove(ctx.channel());
        log.error("异常信息：\r\n" + cause);
    }
}





