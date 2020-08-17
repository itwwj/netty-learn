package com.github.io.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 * 类名称:AioServiceHandler
 * 类描述:
 * 创建人:王文杰
 * 邮箱:275236367@qq.com
 * 创建时间:  2019/11/11 15:04
 */
public class AioServerHandler implements  Runnable {

    public CountDownLatch latch;
    //异步通信通道
    public AsynchronousServerSocketChannel channel;

    public AioServerHandler(int port){
        try {
            //建立服务端通道
            channel=AsynchronousServerSocketChannel.open();
            //绑定端口
            channel.bind(new InetSocketAddress(port));
            System.out.println("server is start,port:"+port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        latch=new CountDownLatch(1);
        //接收客户端连接
        channel.accept(this,new AioAccepHandler());
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
