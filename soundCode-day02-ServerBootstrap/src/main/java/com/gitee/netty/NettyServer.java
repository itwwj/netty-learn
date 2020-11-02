package com.gitee.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * netty服务端启动类
 *
 *  serverBootstrap.bind()调用流程-->
 * 调用ServerBootstrap.bind()：应用调用ServerBootstrap的bind()操作；
 * 调用AbstractBootstrap.bind()：调用doBind()对进行bind操作；
 * 调用AbstractBootstrap.initAndRegister()：利用ChannelFactory.newChannel()实例化NioServerSocketChannel；
 * 调用ServerBootstrap.init()：对NioServerSocketChannel进行初始化，主要操作如设置Channel相关的选项及属性、设置ChannelHandler为ServerBootstrapAcceptor等，ServerBootstrapAcceptor为inbound类型的ChannelHandler，其为ServerBootstrap的内部类，其主要实现ChannelRead()操作，将客户端的连接注册到EventLoopGroup的EventLoop中。
 * 调用NioEventLoop.register()：将NioServerSocketChannel注册到bossGroup中。
 * 调用AbstractBootstrap.doBind0：将实际的bind操作以任务的形式添加到bossGroup的EventLoop中。
 * 调用NioServerSocketChannel.bind()：在EventLoop中以任务的形式调用此方法进行实际的bind()操作。
 * @author jie
 */
public class NettyServer {
    /**
     * 设置端口号
     */
    private static int port = 1100;

    public static void main(String[] args) {
        //用于处理服务端接受客户端连接
        EventLoopGroup boosGroup = new NioEventLoopGroup();
        //用于SocketChannel的网络读写操作
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //用于启动NIO服务端的辅助启动类,目的是降低服务端的开发复杂度
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boosGroup, workerGroup)
                    //对应JDK NIO类库中的ServerSocketChannel
                    .channel(NioServerSocketChannel.class)
                    //配置NioServerSocketChannel的TCP参数
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    //绑定I/O的事件处理类
                    .childHandler(new ChannelInitializer() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {

                        }
                    });
            //调用它的bind操作监听端口号,调用同步阻塞方法sync等待绑定操作完成
            ChannelFuture f = serverBootstrap.bind(port).sync();
            //异步操作的通知回调
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //优雅的退出,释放线程池资源
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
