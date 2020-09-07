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
        String msg = o.toString();
        byte[] bytes = msg.getBytes();

        byte[] send = new byte[bytes.length + 2];
        System.arraycopy(bytes, 0, send, 1, bytes.length);
        send[0] = 0x09;
        send[send.length - 1] = 0x06;

        byteBuf.writeInt(send.length);
        byteBuf.writeBytes(send);
    }
}
