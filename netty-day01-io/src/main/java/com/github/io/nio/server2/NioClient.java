package com.github.io.nio.server2;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author jie
 */
public class NioClient {
    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        if (!socketChannel.connect(new InetSocketAddress("127.0.0.1", 1100))) {
            while (!socketChannel.finishConnect()) {
                System.out.println("连接没有成功，非阻塞循环");
            }
        }
        ByteBuffer buffer = ByteBuffer.wrap("hello nio".getBytes());
        socketChannel.write(buffer);
        System.in.read();
    }
}
