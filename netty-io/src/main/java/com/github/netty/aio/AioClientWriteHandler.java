package com.github.netty.aio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * 类名称:AioClientWriteHandler
 * 类描述: 异步写操作处理器 写完成后会通知调用方
 * 创建人:王文杰
 * 邮箱:275236367@qq.com
 * 创建时间:  2019/11/11 14:05
 */
public class AioClientWriteHandler implements CompletionHandler<Integer, ByteBuffer> {
    private AsynchronousSocketChannel channel;
    private CountDownLatch latch;

    public AioClientWriteHandler(AsynchronousSocketChannel channel, CountDownLatch latch) {
        this.channel = channel;
        this.latch = latch;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {

        if (attachment.hasRemaining()) {//如果没写完
            channel.write(attachment, attachment, this);
        } else { //如果写完 读取数据
            ByteBuffer readBuffer = ByteBuffer.allocate(1024);
            //异步读取数据
            channel.read(readBuffer,readBuffer,new AioClientReadHandler(channel,latch));
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        System.out.println("写入失败");
        try {
            channel.close();
            latch.countDown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
