package com.github.netty.aio;

import java.util.Scanner;

/**
 * 类名称:AioClient
 * 类描述:Aio客户端主程序
 * 创建人:王文杰
 * 邮箱:275236367@qq.com
 * 创建时间:  2019/11/11 11:36
 */
public class AioClient {
    private static AioClientHandler clientHandler;

    public static void start() {
        if (clientHandler != null) {
            return;
        }
        clientHandler = new AioClientHandler("127.0.0.1", 1100);
        new Thread(clientHandler,"Client").start();
    }

    public static boolean sendMsg(String msg){
        if (msg.equals("q")){
            return false;
        }
        clientHandler.sendMessage(msg);
       return true;
    }
    public static void main(String[] args) throws Exception{
        AioClient.start();
        System.out.println("请输入请求消息：");
        Scanner scanner = new Scanner(System.in);
        while(AioClient.sendMsg(scanner.nextLine()));
    }
}
