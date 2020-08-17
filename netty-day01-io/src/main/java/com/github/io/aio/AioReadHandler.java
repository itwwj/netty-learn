package com.github.io.aio;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * 类名称:AioReadHandler
 * 类描述:
 * 创建人:王文杰
 * 邮箱:275236367@qq.com
 * 创建时间:  2019/11/11 15:05
 */
public class AioReadHandler implements CompletionHandler<Integer, ByteBuffer> {
    private AsynchronousSocketChannel channel;

    public AioReadHandler(AsynchronousSocketChannel channel) {
        this.channel = channel;
    }

    /**
     * 读取到消息后的处理
     *
     * @param result
     * @param attachment
     */
    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        if (result == -1) {//如果条件成立,说明客户端主动终止了tcp套接字,这时服务端终止就可以了
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        attachment.flip();
        byte[] bytes = new byte[attachment.remaining()];
        attachment.get(bytes);
        String msg;
        try {
            System.out.println(result);
            msg = new String(bytes, "UTF-8");
            System.out.println("server accept message:" + msg);
            String response = Ch01Const.response(msg);
            doWrite(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doWrite(String result) {
        byte[] bytes = result.getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        writeBuffer.put(bytes);
        writeBuffer.flip();
        channel.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                if (attachment.hasRemaining()) {//如果没写完
                    channel.write(attachment, attachment, this);
                } else { //如果写完 读取数据
                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                    //异步读取数据
                    channel.read(readBuffer,readBuffer,new AioReadHandler(channel));
                }
            }
            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        try {
            this.channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
