package com.github.io.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * 类名称:AioAccepHandler
 * 类描述:
 * 创建人:王文杰
 * 邮箱:275236367@qq.com
 * 创建时间:  2019/11/11 15:05
 */
public class AioAccepHandler implements CompletionHandler<AsynchronousSocketChannel, AioServerHandler> {

    @Override
    public void completed(AsynchronousSocketChannel result, AioServerHandler attachment) {
        AioServer.clientCount++;
        System.out.println("链接的客户端数:" + AioServer.clientCount);
        //重新注册监听,让别的客户端也可以连接
        attachment.channel.accept(attachment, this);
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        //异步读取
        //1)ByteBuffer dst：接收缓冲区，用于从异步Channel中读取数据包；
        //2)  A attachment：异步Channel携带的附件，通知回调的时候作为入参使用；
        //3)  CompletionHandler<Integer,? super A>：系统回调的业务handler，进行读操作
        result.read(readBuffer,readBuffer,new AioReadHandler(result));

    }

    @Override
    public void failed(Throwable exc, AioServerHandler attachment) {
        exc.printStackTrace();
        attachment.latch.countDown();
    }
}
