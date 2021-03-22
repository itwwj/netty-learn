package com.github.io.nio.client;

import com.github.io.nio.server.HexConvertUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 项目名称: Study
 * 类名称:NioClientHandler
 * 类描述:nio通信客户端处理器
 * 创建人:王文杰
 * 邮箱:275236367@qq.com
 * 创建时间:  2019/11/12 15:09
 */
public class NioClientHandler implements Runnable {
    private String host;
    private int port;
    private Selector selector;
    private SocketChannel channel;

    /**
     * 设置或获取一个值表示nio是否打开
     */
    private volatile boolean started;


    public void stop(){
        started = false;
    }
    public NioClientHandler(String host, int port) {
        this.host = host;
        this.port = port;
        try {
            this.selector = Selector.open();
            this.channel = SocketChannel.open();
            channel.configureBlocking(false);//开启非阻塞模式
            started = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @throws IOException
     */
    private void doConnect() throws IOException {
        //判断是否成功链接服务端 成功链接true
        /* connect:
         *   如果此通道处于非阻塞模式，则此方法的调用将启动非阻塞连接操作。
         *   如果像本地连接那样立即建立连接，则此方法返回true。否则，此方法返回false，
         *   并且稍后必须通过调用finishConnect方法来完成连接操作。
         * */
        if (channel.connect(new InetSocketAddress(host, port))) {
        } else {
            //通知选择器链接已建立
            channel.register(selector, SelectionKey.OP_CONNECT);
        }
    }


    @Override
    public void run() {
        try {
            doConnect();
        } catch (IOException e) {
            e.printStackTrace();
            //如果有异常程序退出
            System.exit(1);
        }
        while (started) {
            try {
                //轮询 检查当前是否有事件发生  没有事件发生阻塞
                selector.select();
                //拿到事件集合
                Set<SelectionKey> keys = selector.selectedKeys();
                //迭代器
                Iterator<SelectionKey> it = keys.iterator();
                SelectionKey key = null;
                while (it.hasNext()) {
                    key = it.next();
                    it.remove();
                    try {
                        handleInput(key);
                    } catch (IOException e) {
                        e.printStackTrace();
                        if (key != null) {
                            key.cancel();
                            if (key.channel() != null) {
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param key
     * @throws IOException
     */
    private void handleInput(SelectionKey key) throws IOException {
        //检查key是否有效
        if (key.isValid()) {
            SocketChannel sc = (SocketChannel) key.channel();
            //是否为链接事件
            if (key.isConnectable()) {
                if (sc.finishConnect()) {
                } else {
                    System.exit(1);
                }
            }
            //是否为读事件
            if (key.isReadable()) {
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                int read = sc.read(buffer);
                if (read > 0) {
                    buffer.flip();
                    byte[] bytes = new byte[buffer.remaining()];
                    buffer.get(bytes);
                    String result = HexConvertUtil.BinaryToHexString(bytes);
                    System.out.println("accept message:" + result);
                } else if (read < 0) {
                    //取消通道
                    key.cancel();
                    sc.close();
                }
            }
        }
    }


    public void sendMsg(String Msg) throws IOException {
        channel.register(selector, SelectionKey.OP_READ);
        doWrite(channel, Msg);
    }

    /**
     * 发送消息
     *
     * @param channel
     * @param request
     * @throws IOException
     */
    private void doWrite(SocketChannel channel, String request)
            throws IOException {
        //将消息编码为字节数组
        byte[] bytes = HexConvertUtil.hexStringToBytes(request);
        //根据数组容量创建ByteBuffer
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        //将字节数组复制到缓冲区
        writeBuffer.put(bytes);
        //flip操作
        writeBuffer.flip();
        //发送缓冲区的字节数组
        channel.write(writeBuffer);
    }
}
