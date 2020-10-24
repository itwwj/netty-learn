package com.gitee.iot.util;

import io.netty.buffer.ByteBuf;

/**
 * <p>跨线程情况下 byteBuf 需要转换成byte[]
 * @author jie
 */
@SuppressWarnings("ALL")
public class ByteBufUtil {
    public static byte[] copyByteBuf(ByteBuf byteBuf) {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        return bytes;
    }

    public static String bufToString(ByteBuf buf) {
        String str;
        // 处理堆缓冲区
        if (buf.hasArray()) {
            str = new String(buf.array(), buf.arrayOffset() + buf.readerIndex(), buf.readableBytes());
            // 处理直接缓冲区以及复合缓冲区
        } else {
            byte[] bytes = new byte[buf.readableBytes()];
            buf.getBytes(buf.readerIndex(), bytes);
            str = new String(bytes, 0, buf.readableBytes());
        }
        return str;
    }
}
