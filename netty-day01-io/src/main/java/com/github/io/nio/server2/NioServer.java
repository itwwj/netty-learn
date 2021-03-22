package com.github.io.nio.server2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @author jie
 */
public class NioServer {
    public static void main(String[] args) throws IOException {
        //创建serverSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //创建selector
        Selector selector = Selector.open();
        //绑定端口1100
        serverSocketChannel.socket().bind(new InetSocketAddress(1100));
        //设置成非阻塞
        serverSocketChannel.configureBlocking(false);
        //将serverSocketChannel注册到selector
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        //循环等待客户端连接
        while (true) {
            //阻塞1s如果没有事件就跳出本次循环
            if (selector.select(1000) == 0) {
                continue;
            }
            //如果返回>0,表示以获取到关注的事件 获取selectionKeys集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey key=iterator.next();
                //判断当前的事件是否是key的注册事件
                if (key.isAcceptable()) {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    //设置为非阻塞
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                    System.out.println("收到连接。。。" + serverSocketChannel.hashCode());
                    //判断当前的事件是否是当前SelectionKey的读事件
                } else if (key.isReadable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
                    channel.read(byteBuffer);
                    System.out.println("服务端收到：" + new String(byteBuffer.array()));
                }
                iterator.remove();
            }
        }
    }
}
