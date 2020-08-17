package com.github.io.nio;

/**
 * TODO
 *
 * @author jie
 * @date 2020/8/14 22:47
 */
public class NioServer {
    private static NioServerHandler nioServerHandle;

    public static void start(){
        if(nioServerHandle !=null) {
            nioServerHandle.stop();
        }
        nioServerHandle = new NioServerHandler(1100);
        new Thread(nioServerHandle,"Server").start();
    }
    public static void main(String[] args){
        start();
    }
}
