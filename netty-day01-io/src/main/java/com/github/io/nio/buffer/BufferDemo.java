package com.github.io.nio.buffer;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * @author jie
 */
public class BufferDemo {
    public static void main(String[] args) throws Exception {
        //bufferPut();
        // mappedBuffer();
        scatteringAndGathering();
    }

    /**
     * 注意取得顺序和放入的顺序保持一致
     * 类型化
     */
    private static void bufferPut() {
        ByteBuffer buffer = ByteBuffer.allocate(512);
        buffer.putInt(1024);
        buffer.putChar('a');
        buffer.putLong(2048L);
        buffer.putShort((short) 128);
        buffer.putDouble(10.24);
        buffer.putFloat(5.12f);
        buffer.flip();
        System.out.println(buffer.getInt());
        System.out.println(buffer.getChar());
        System.out.println(buffer.getLong());
        System.out.println(buffer.getShort());
        System.out.println(buffer.getDouble());
        System.out.println(buffer.getFloat());
    }

    /**
     * 可以让文件直接在内存（堆外）修改 操作系统不需要将文件拷贝再拷贝一次
     */
    private static void mappedBuffer() throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("E:\\data\\coap.txt", "rw");
        FileChannel channel = randomAccessFile.getChannel();
        /**
         * 参数1：FileChannel.MapMode.READ_WRITE 读写模式
         * 参数2： 0 直接修改的起始位置
         * 参数3： 5 映射到内存的大小 最多映射5个字节
         * 可以修改的范围就0-5
         */
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        mappedByteBuffer.put(0, (byte) 'A');
        randomAccessFile.close();
    }



    /**
     * 将数据写入buffer中时，可以使用buffer数组，依次写入
     * 从buffer中读取数据时 可以放入buffer数组
     */
    private static void scatteringAndGathering() throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress address = new InetSocketAddress(1100);
        serverSocketChannel.socket().bind(address);

        ByteBuffer[] buffers = new ByteBuffer[2];
        buffers[0] = ByteBuffer.allocate(5);
        buffers[1] = ByteBuffer.allocate(3);
        SocketChannel socketChannel = serverSocketChannel.accept();

        while (true) {
            int byteRead = 0;
            while (byteRead < 8) {
                long read = socketChannel.read(buffers);
                byteRead++;
                Arrays.stream(buffers).map(buffer -> "position: " + buffer.position() + "  limit: " + buffer.limit()).forEach(System.out::println);
            }

            Arrays.asList(buffers).forEach(Buffer::flip);
            int byteWrit = 0;
            while (byteWrit < 8) {
                long write = socketChannel.write(buffers);
                byteWrit++;
            }
            Arrays.asList(buffers).forEach(Buffer::clear);
            System.out.println("byteRead: "+byteRead+" byteWrit: "+byteWrit);
        }
    }
}
