package com.github.custom.codec;

import com.github.custom.utils.HexConvertUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * 自定义解码器
 *
 * @author jie
 */
public class CustomDecoder extends ByteToMessageDecoder {

    private final int BASE_LENGTH = 2;

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
        //报文基本长度包含一个帧头一个结尾最少为2
        if (in.readableBytes() < BASE_LENGTH) {
            return;
        }
        int start; //记录帧头位置
        while (true) {
            // 获取帧头开始的index
            start = in.readerIndex();
            // 将当前的readerIndex备份到markedReaderIndex中
            in.markReaderIndex();
            // 读到了协议的开始标志，结束while循环
            byte header = in.readByte();
            if (header == 0x0A) {
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
        //剩余长度不足1，没有数据长度帧位
        int readableCount = in.readableBytes();
        if (readableCount <= 1) {
            in.readerIndex(start);
            return;
        }
        //数据长度帧位
        byte len = in.readByte();
        String s = Byte.toString(len);
        Long size = HexConvertUtil.hexToLong(s);
        //剩余长度不足可读取数量[没有帧尾标识]
        readableCount = in.readableBytes();
        if (readableCount < size + 1) {
            in.readerIndex(start);
            return;
        }
        ByteBuf msg = in.readBytes(Math.toIntExact(size));
        //如果没有帧尾标识，还原指针位置[其他标识结尾]
        byte end = in.readByte();
        if (end != 0x0B) {
            in.readerIndex(start);
            return;
        }
        list.add(msg.toString(CharsetUtil.UTF_8));
    }
}
