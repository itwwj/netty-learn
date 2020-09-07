package com.github.custom.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.nio.charset.Charset;
import java.util.List;

/**
 * 自定义解码器
 * @author jie
 */
public class CustomDecoder extends ByteToMessageDecoder {
    /**
     *十六进制字符串数组解析
     * AA为开始符
     * AB为结束符
     * @param channelHandlerContext
     * @param byteBuf 传入数据
     * @param list 添加解码消息
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        //基础长度不足，我们设定基础长度为4
        if (byteBuf.readableBytes() < 4) {
            return;
        }

        int beginIndex; //记录包头位置

        while (true) {
            // 获取包头开始的index
            beginIndex = byteBuf.readerIndex();
            // 标记包头开始的index
            byteBuf.markReaderIndex();
            // 读到了协议的开始标志，结束while循环
            if (byteBuf.readByte() == 0x09) {
                break;
            }
            // 未读到包头，略过一个字节
            // 每次略过，一个字节，去读取，包头信息的开始标记
            byteBuf.resetReaderIndex();
            byteBuf.readByte();
            // 当略过，一个字节之后，
            // 数据包的长度，又变得不满足
            // 此时，应该结束。等待后面的数据到达
            if (byteBuf.readableBytes() < 4) {
                return;
            }
        }

        //剩余长度不足可读取数量[没有内容长度位]
        int readableCount = byteBuf.readableBytes();
        if (readableCount <= 1) {
            byteBuf.readerIndex(beginIndex);
            return;
        }
        //长度域占4字节，读取int
        ByteBuf bf = byteBuf.readBytes(1);
        String msgLengthStr = bf.toString(Charset.forName("utf8"));
        int msgLength = Integer.parseInt(msgLengthStr);

        //剩余长度不足可读取数量[没有结尾标识]
        readableCount = byteBuf.readableBytes();
        if (readableCount < msgLength + 1) {
            byteBuf.readerIndex(beginIndex);
            return;
        }
        ByteBuf msgContent = byteBuf.readBytes(msgLength);
        //如果没有结尾标识，还原指针位置[其他标识结尾]
        byte end = byteBuf.readByte();
        if (end != 0x06) {
            byteBuf.readerIndex(beginIndex);
            return;
        }
        list.add(msgContent.toString(Charset.forName("utf8")));
    }
}
