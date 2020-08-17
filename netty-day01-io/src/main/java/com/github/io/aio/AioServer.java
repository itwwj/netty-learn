package com.github.io.aio;

/**
 * 类名称:AioService
 * 类描述:服务端主类
 * 创建人:王文杰
 * 邮箱:275236367@qq.com
 * 创建时间:  2019/11/11 15:03
 */
public class AioServer {
    private static AioServerHandler serverHandler;
    public static int clientCount = 0;

    public static void start() {
        if (serverHandler != null) {
            return;
        }
        serverHandler = new AioServerHandler(1100);
        new Thread(serverHandler, "Server").start();
    }

    public static void main(String[] args) {
        AioServer.start();
    }
}
