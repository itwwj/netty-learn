package com.gitee.netty.test;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author jie
 */
public class BootTest {

    public static void main(String[] args) {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.AUTO_READ, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel channel) throws Exception {
                    channel.pipeline().addLast(new LineBasedFrameDecoder(1024));
                    channel.pipeline().addLast(new StringDecoder(Charset.forName("utf8")));
                    channel.pipeline().addLast(new StringEncoder(Charset.forName("utf8")));
                    channel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 客户端接收到消息：" + msg);
                        }
                    });
                }
            });
            ChannelFuture f = b.connect("127.0.0.1", 1100).sync();

            f.channel().writeAndFlush("客户端消息1\r\n");
            f.channel().writeAndFlush("客户端消息2\r\n");
            f.channel().writeAndFlush("客户端消息3\r\n");
            f.channel().writeAndFlush("客户端消息4\r\n");
            f.channel().writeAndFlush("客户端消息5\r\n");
            f.channel().writeAndFlush("客户端消息6\r\n");

            f.channel().closeFuture().syncUninterruptibly();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
