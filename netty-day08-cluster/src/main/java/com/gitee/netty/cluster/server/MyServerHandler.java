package com.gitee.netty.cluster.server;

import com.gitee.netty.cluster.model.MsgAgreement;
import com.gitee.netty.cluster.model.DeviceChannelInfo;
import com.gitee.netty.cluster.utils.CacheUtil;
import com.gitee.netty.cluster.utils.ExtServerService;
import com.gitee.netty.cluster.utils.MsgUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;


/**
 * @author jie
 */
@Slf4j
public class MyServerHandler extends ChannelInboundHandlerAdapter {


    private ExtServerService extServerService;

    public MyServerHandler(ExtServerService extServerService) {
        this.extServerService = extServerService;
    }

    /**
     * 当客户端主动链接服务端的链接后，这个通道就是活跃的了。也就是客户端与服务端建立了通信通道并且可以传输数据
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SocketChannel channel = (SocketChannel) ctx.channel();
        log.info("有一客户端链接到本服务端 channelId:" + channel.id() + " IP:" + channel.localAddress().getHostString());
        //保存设备信息
        DeviceChannelInfo deviceChannelInfo = new DeviceChannelInfo(channel.localAddress().getHostString(), channel.localAddress().getPort(), channel.id().toString(), new Date());
        extServerService.getRedisUtil().pushObj(deviceChannelInfo);
        CacheUtil.cacheChannel.put(channel.id().toString(), channel);
        ctx.writeAndFlush(MsgUtil.buildMsg(channel.id().toString(), "ok"));
    }

    /**
     * 当客户端主动断开服务端的链接后，这个通道就是不活跃的。也就是说客户端与服务端的关闭了通信通道并且不可以传输数据
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端断开链接" + ctx.channel().localAddress().toString());
        extServerService.getRedisUtil().remove(ctx.channel().id().toString());
        CacheUtil.cacheChannel.remove(ctx.channel().id().toString(), ctx.channel());

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object objMsgJsonStr) throws Exception {

        MsgAgreement msgAgreement = MsgUtil.json2Obj(objMsgJsonStr.toString());
        String toChannelId = msgAgreement.getToChannelId();
        //判断接收消息用户是否在本服务端
        Channel channel = CacheUtil.cacheChannel.get(toChannelId);
        if (null != channel) {
            channel.writeAndFlush(MsgUtil.obj2Json(msgAgreement));
            return;
        }
        //如果为NULL则接收消息的用户不在本服务端，需要push消息给全局
        log.info("接收消息的用户不在本服务端，PUSH！");
        extServerService.push(msgAgreement);
    }

    /**
     * 抓住异常，当发生异常的时候，可以做一些相应的处理，比如打印日志、关闭链接
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        extServerService.getRedisUtil().remove(ctx.channel().id().toString());
        CacheUtil.cacheChannel.remove(ctx.channel().id().toString(), ctx.channel());
        log.error("异常信息：\r\n" + cause.getMessage());
    }
}
