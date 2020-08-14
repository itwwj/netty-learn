package com.github.netty.aio.client;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * 类名称:AioClientHandler
 * 类描述:接收链接处理器
 * 创建人:王文杰
 * 邮箱:275236367@qq.com
 * 创建时间:  2019/11/11 11:51
 */
public class AioClientHandler implements CompletionHandler<Void, AioClientHandler>,Runnable {
    //客户端异步操作的Socket
    private AsynchronousSocketChannel channel;
    //ip地址
    private String host;
    //端口号
    private int port;
    //阻塞方法 阻塞客户端 防止应用程序退出
    private CountDownLatch latch;


    public AioClientHandler(String host, int port) {
        this.host = host;
        this.port = port;
        try {
            //创建客户端通道
            channel=AsynchronousSocketChannel.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 链接成功时调用
     * @param result
     * @param attachment
     */
    @Override
    public void completed(Void result, AioClientHandler attachment) {
        System.out.println("链接到服务端");
    }


    /**
     * 连接失败时调用
     * @param exc
     * @param attachment
     */
    @Override
    public void failed(Throwable exc, AioClientHandler attachment) {
        System.out.println("链接错误");
        //打印失败原因
        exc.printStackTrace();
        latch.countDown();
        try {
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        latch=new CountDownLatch(1);
        channel.connect(new InetSocketAddress(host,port),this,this);
        try {
            latch.await();
            channel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送数据
     * @param msg
     */
    public void sendMessage(String msg){
        byte[] bytes = msg.getBytes();
        ByteBuffer buffer=ByteBuffer.allocate(bytes.length);
        buffer.put(bytes);
        buffer.flip();
        //异步写
        channel.write(buffer,buffer,new AioClientWriteHandler(channel,latch));
    }
}
