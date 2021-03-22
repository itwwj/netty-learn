package com.github.io.nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * 类名称:NioServerHandler
 * 类描述:服务端处理类
 * 邮箱:275236367@qq.com
 * 创建时间:  2019/11/12 15:10
 * @author jie
 */
public class NioServerHandler implements Runnable {
    private Selector selector;
    private ServerSocketChannel channel;
    private volatile boolean started;

    public NioServerHandler(int port) {
        try {
            selector = Selector.open();
            channel = ServerSocketChannel.open();
            //打开非阻塞模式
            channel.configureBlocking(false);
            channel.bind(new InetSocketAddress(port));
            //注册事件
            channel.register(selector, SelectionKey.OP_ACCEPT);
            started = true;
            System.out.println("服务器已启动,端口号:" + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        started = false;
    }

    @Override
    public void run() {
        //循环遍历selector
        while (started) {
            try {
                //无论是否有读写事件发生，selector每隔1s被唤醒一次
                selector.select(1000);
                //阻塞,只有当至少一个注册的事件发生的时候才会继续.
                //selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> it = keys.iterator();
                SelectionKey key = null;
                while (it.hasNext()) {
                    key = it.next();
                    it.remove();
                    try {
                        handleInput(key);
                    } catch (Exception e) {
                        if (key != null) {
                            key.cancel();
                            if (key.channel() != null) {
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        //selector关闭后会自动释放里面管理的资源
        if (selector != null) {
            try {
                selector.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey key) throws IOException {
        if (key.isValid()) {
            //处理新接入的连接
            if (key.isAcceptable()) {
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                SocketChannel sc = ssc.accept();
                System.out.println("==========已建立链接==========");
                sc.configureBlocking(false);//开启非阻塞
                sc.register(selector, SelectionKey.OP_READ);
            }
            //读消息
            if (key.isReadable()) {
                System.out.println("======socket channel 数据准备完成，" +
                        "可以去读==读取=======");
                SocketChannel sc = (SocketChannel) key.channel();
                //创建ByteBuffer，并开辟一个1M的缓冲区
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                //读取请求码流，返回读取到的字节数
                int readBytes = sc.read(buffer);
                //读取到字节，对字节进行编解码
                if (readBytes > 0) {
                    //将缓冲区当前的limit设置为position=0，
                    // 用于后续对缓冲区的读取操作
                    buffer.flip();
                    //根据缓冲区可读字节数创建字节数组
                    byte[] bytes = new byte[buffer.remaining()];
                    //将缓冲区可读字节数组复制到新建的数组中
                    buffer.get(bytes);
                    String message = HexConvertUtil.BinaryToHexString(bytes);
                    System.out.println("服务器收到消息："+new Date()+ "          " + message);
                    //处理数据
                    String result = null;
                    try {
                        result = message;
                    } catch (Exception e) {
                        result = "计算错误：" + e.getMessage();
                    }
                    /*发送应答消息*/
                    doWrite(sc, result);
                    /* for (int i = 0; i < 5; i++) {
                     *//*发送应答消息*//*
                        doWrite(sc, result);
                    }*/
                }
                //没有读取到字节 忽略
//				else if(readBytes==0);
                //链路已经关闭，释放资源
                else if (readBytes < 0) {
                    key.cancel();
                    sc.close();
                }
            }
        }
    }

    private void doWrite(SocketChannel channel, String response)
            throws IOException {
        //将消息编码为字节数组
        byte[] bytes = HexConvertUtil.hexStringToBytes(response);

       // byte[] s = "/n".getBytes();

        //根据数组容量创建ByteBuffer
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        //将字节数组复制到缓冲区
        writeBuffer.put(bytes);
        //flip操作
        writeBuffer.flip();
        //发送缓冲区的字节数组
        channel.write(writeBuffer);
    }

    private byte[] addBytes(byte[] arr1, byte[] arr2) {
        byte[] arr3 = new byte[arr1.length + arr2.length];
        System.arraycopy(arr1, 0, arr3, 0, arr1.length);
        System.arraycopy(arr2, 0, arr3, arr1.length, arr1.length);
        return arr3;
    }

}
