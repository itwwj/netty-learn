package com.github.netty.nio;

import java.util.Scanner;

/**
 * 项目名称: Study
 * 类名称:NioClient
 * 类描述:Nio客户端主类
 * 创建人:王文杰
 * 邮箱:275236367@qq.com
 * 创建时间:  2019/11/12 15:09
 */
public class NioClient {
    private static NioClientHandler nioClientHandle;
    public static void start(){
        System.out.println("客户端启动");
        if(nioClientHandle !=null) {
            nioClientHandle.stop();
        }
        nioClientHandle = new NioClientHandler("127.0.0.1",1100);
        new Thread(nioClientHandle,"Server").start();
    }
    //向服务器发送消息
    public static boolean sendMsg(String msg) throws Exception{
        nioClientHandle.sendMsg(msg);
        return true;
    }
    public static void main(String[] args) throws Exception {
        start();
        Scanner scanner = new Scanner(System.in);
        while(NioClient.sendMsg(scanner.nextLine()));
    }
}
