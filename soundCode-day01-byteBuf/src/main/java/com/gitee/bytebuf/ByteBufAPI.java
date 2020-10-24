package com.gitee.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Arrays;

/**
 * @author jie
 */
public class ByteBufAPI {
    public static void main(String[] args) {
// 1.创建一个非池化的ByteBuf，大小为14个字节
        ByteBuf buffer = Unpooled.buffer(14);
        System.out.println("1.创建一个非池化的ByteBuf，大小为14个字节");
        System.out.println("ByteBuf空间大小：" + buffer.capacity());
        // 2.写入3个字节
        buffer.writeByte(62);
        buffer.writeByte(75);
        buffer.writeByte(67);
        System.out.println("\r\n2.写入3个字节");
        System.out.println("readerIndex位置：" + buffer.readerIndex());
        System.out.println("writerIndex位置：" + buffer.writerIndex());
        // 3.写入一段字节
        byte[] bytes = {73, 74, 61, 63, 0x6B};
        buffer.writeBytes(bytes);
        System.out.println("\r\n3.写入一段字节");
        System.out.println("readerIndex位置：" + buffer.readerIndex());
        System.out.println("writerIndex位置：" + buffer.writerIndex());
        // 4.读取全部内容
        byte[] allBytes = new byte[buffer.readableBytes()];
        buffer.readBytes(allBytes);
        System.out.println("\r\n4.读取全部内容");
        System.out.println("readerIndex位置：" + buffer.readerIndex());
        System.out.println("writerIndex位置：" + buffer.writerIndex());
        System.out.println("读取全部内容：" + Arrays.toString(allBytes));
        // 5.重置指针位置
        buffer.resetReaderIndex();
        System.out.println("\r\n5.重置指针位置");
        System.out.println("readerIndex位置：" + buffer.readerIndex());
        System.out.println("writerIndex位置：" + buffer.writerIndex());
        // 6.读取3个字节
        byte b0 = buffer.readByte();
        byte b1 = buffer.readByte();
        byte b2 = buffer.readByte();
        System.out.println("\r\n6.读取3个字节");
        System.out.println("readerIndex位置：" + buffer.readerIndex());
        System.out.println("writerIndex位置：" + buffer.writerIndex());
        System.out.println("读取3个字节：" + Arrays.toString(new byte[]{b0, b1, b2}));
        // 7.读取一段字节
        ByteBuf byteBuf = buffer.readBytes(5);
        byte[] dst = new byte[5];
        byteBuf.readBytes(dst);
        System.out.println("\r\n7.读取一段字节");
        System.out.println("readerIndex位置：" + buffer.readerIndex());
        System.out.println("writerIndex位置：" + buffer.writerIndex());
        System.out.println("读取一段字节：" + Arrays.toString(dst));
        // 8.丢弃已读内容
        buffer.discardReadBytes();
        System.out.println("\r\n8.丢弃已读内容");
        System.out.println("readerIndex位置：" + buffer.readerIndex());
        System.out.println("writerIndex位置：" + buffer.writerIndex());
        // 9.清空指针位置
        buffer.clear();
        System.out.println("\r\n9.清空指针位置");
        System.out.println("readerIndex位置：" + buffer.readerIndex());
        System.out.println("writerIndex位置：" + buffer.writerIndex());
        // 10.ByteBuf中还有很多其他方法；拷贝、标记、跳过字节，多用于自定义解码器进行半包粘包处理

    }
}
