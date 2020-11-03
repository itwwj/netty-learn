package com.github.custom.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 自定义编码器
 * @author jie
 */
public class CustomEncoder extends MessageToByteEncoder {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        //消息原始内容
        String msg = o.toString();
        byte[] bytes = msg.getBytes();
        //定义一个新的数组 长度为原始内容长度+帧头+长度帧位+帧尾
        byte[] send = new byte[bytes.length + 3];
        //将原始内容数组复制到新的数组
        System.arraycopy(bytes, 0, send, 2, bytes.length);
        send[0] = 0x0A;
        send[1]=Byte.parseByte(String.valueOf(bytes.length));
        send[send.length - 1] = 0x0B;

        byteBuf.writeInt(send.length);
        byteBuf.writeBytes(send);
    }
}
