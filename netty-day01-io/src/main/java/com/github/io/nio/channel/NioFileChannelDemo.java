package com.github.io.nio.channel;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 使用nio拷贝文件
 *
 * @author jie
 */
public class NioFileChannelDemo {
    static String path = "E:\\data\\nioFile.txt";

    public static void main(String[] args) throws IOException {
        //writ();
        //read();

        //copy();
        zeroCopy();
    }

    private static void read() throws IOException {
        File file = new File(path);
        FileInputStream inputStream = new FileInputStream(file);
        FileChannel channel = inputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());
        channel.read(byteBuffer);
        System.out.println(new String(byteBuffer.array()));
        inputStream.close();
    }

    private static void writ() throws IOException {
        String str = "hello nio channel";
        FileOutputStream outputStream = new FileOutputStream(path);
        FileChannel channel = outputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put(str.getBytes());
        byteBuffer.flip();
        channel.write(byteBuffer);
        outputStream.close();
    }

    private static void copy() throws IOException {
        long start = System.currentTimeMillis();
        FileInputStream inputStream = new FileInputStream("D:\\360MoveData\\Users\\jie\\Desktop\\Netty权威指南第2版.pdf");
        FileOutputStream outputStream = new FileOutputStream("E:\\data\\coap.pdf");
        FileChannel inputStreamChannel = inputStream.getChannel();
        FileChannel outputStreamChannel = outputStream.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (inputStreamChannel.read(buffer) != -1) {
            buffer.flip();
            outputStreamChannel.write(buffer);
            buffer.clear();
        }
        long end = System.currentTimeMillis();
        System.out.println("耗时： "+(end-start)+" ms");
        inputStream.close();
        outputStream.close();
    }
    private static void zeroCopy() throws IOException {
        long start = System.currentTimeMillis();
        FileInputStream zeroInputStream = new FileInputStream("D:\\360MoveData\\Users\\jie\\Desktop\\Netty权威指南第2版.pdf");
        FileOutputStream zeroOutputStream = new FileOutputStream("E:\\data\\zeroCoap.pdf");
        FileChannel isc = zeroInputStream.getChannel();
        FileChannel osc = zeroOutputStream.getChannel();
        osc.transferFrom(isc, 0, isc.size());
        long end = System.currentTimeMillis();
        System.out.println("耗时： "+(end-start)+" ms");
        isc.close();
        osc.close();
    }
}
