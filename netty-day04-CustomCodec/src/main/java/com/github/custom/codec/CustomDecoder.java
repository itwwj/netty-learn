package com.github.custom.codec;

import com.github.custom.utils.HexConvertUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.Charset;
import java.util.List;

/**
 * 自定义解码器
 *
 * @author jie
 */
public class CustomDecoder extends ByteToMessageDecoder {

    private final int BASE_LENGTH = 4;

    /**
     * 十六进制字符串数组解析
     *
     * @param channelHandlerContext
     * @param in                    传入数据
     * @param list                  添加解码消息
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> list) throws Exception {
        //基础长度不足，我们设定基础长度为4
        if (in.readableBytes() < BASE_LENGTH) {
            return;
        }

        int beginIdx; //记录包头位置

        while (true) {
            // 获取包头开始的index
            beginIdx = in.readerIndex();
            // 将当前的readerIndex备份到markedReaderIndex中
            in.markReaderIndex();
            // 读到了协议的开始标志，结束while循环
            ByteBuf byteBuf = in.readBytes(1);
            byte[] bytes = new byte[byteBuf.readableBytes()];
            String s = HexConvertUtil.BinaryToHexString(bytes);
            Long l = HexConvertUtil.hexToLong(s);
            if (l == 170) {
                break;
            }
            //不是帧头就还原为备份位置
            //将当前的readerIndex重置为markedReaderIndex的值
            in.resetReaderIndex();
            // 略过一个字节
            in.readByte();
            if (in.readableBytes() < BASE_LENGTH) {
                return;
            }
        }

        //剩余长度不足可读取数量[没有内容长度位]
        int readableCount = in.readableBytes();
        if (readableCount <= 1) {
            in.readerIndex(beginIdx);
            return;
        }

        //长度域占4字节，读取int
        ByteBuf byteBuf = in.readBytes(1);
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        Long l = HexConvertUtil.hexToLong(HexConvertUtil.BinaryToHexString(bytes));

        //剩余长度不足可读取数量[没有结尾标识]
        readableCount = in.readableBytes();
        if (readableCount < l + 1) {
            in.readerIndex(beginIdx);
            return;
        }

        ByteBuf msgContent = in.readBytes(Math.toIntExact(l));

        //如果没有结尾标识，还原指针位置[其他标识结尾]
        byte end = in.readByte();
        if (end != 0x55) {
            in.readerIndex(beginIdx);
            return;
        }
        byte[] array = msgContent.array();
        list.add(HexConvertUtil.BinaryToHexString(array));
    }
}
