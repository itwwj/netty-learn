package com.gitee.netty.cluster.server;

import com.gitee.netty.cluster.model.MsgAgreement;
import com.gitee.netty.cluster.model.DeviceChannelInfo;
import com.gitee.netty.cluster.utils.CacheUtil;
import com.gitee.netty.cluster.utils.CacheService;
import com.gitee.netty.cluster.utils.MsgUtil;
import com.gitee.netty.cluster.utils.NetWorkUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;

import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;


/**
 * @author jie
 */
@Slf4j
public class MyServerHandler extends ChannelInboundHandlerAdapter {


    private CacheService cacheService;

    public MyServerHandler(CacheService cacheService) {
        this.cacheService = cacheService;
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                log.info("客户端" + ctx.channel().id() + "长时间未通讯，即将剔除。");
                // 在规定时间内没有收到客户端的上行数据, 主动断开连接
                ctx.disconnect();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    /**
     * 当客户端主动链接服务端的链接后，这个通道就是活跃的了。也就是客户端与服务端建立了通信通道并且可以传输数据
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SocketChannel channel = (SocketChannel) ctx.channel();
        log.info("有一客户端链接到本服务端 channelId:" + channel.id());
        //保存设备信息
        DeviceChannelInfo deviceChannelInfo = DeviceChannelInfo.builder()
                .channelId(channel.id().toString())
                .ip(NetWorkUtils.getHost())
                .port(channel.localAddress().getPort())
                .linkDate(new Date())
                .build();

        cacheService.getRedisUtil().pushObj(deviceChannelInfo);
        CacheUtil.cacheChannel.put(channel.id().toString(), channel);
        ctx.writeAndFlush("ok \r\n");
    }

    /**
     * 当客户端主动断开服务端的链接后，这个通道就是不活跃的。也就是说客户端与服务端的关闭了通信通道并且不可以传输数据
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端断开链接" + ctx.channel().id());
        cacheService.getRedisUtil().remove(ctx.channel().id().toString());
        CacheUtil.cacheChannel.remove(ctx.channel().id().toString(), ctx.channel());
    }

    /**
     * 处理通道内的数据
     *
     * @param ctx
     * @param objMsgJsonStr
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object objMsgJsonStr) throws Exception {
        String msg = objMsgJsonStr.toString();
        if (msg.length() < 5) {
            log.info("心跳消息：" + msg);
            return;
        }
        MsgAgreement msgAgreement = MsgUtil.json2Obj(msg);
        String toChannelId = msgAgreement.getToChannelId();
        //判断接收消息用户是否在本服务端
        Channel channel = CacheUtil.cacheChannel.get(toChannelId);
        if (null != channel) {
            channel.writeAndFlush(MsgUtil.obj2Json(msgAgreement));
            return;
        }
        //如果为NULL则接收消息的用户不在本服务端，需要push消息给全局
        cacheService.push(msgAgreement);
    }

    /**
     * 发现异常关闭连接打印日志
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        cacheService.getRedisUtil().remove(ctx.channel().id().toString());
        CacheUtil.cacheChannel.remove(ctx.channel().id().toString(), ctx.channel());
        log.error("异常信息：\r\n" + cause.getMessage());
    }
}
