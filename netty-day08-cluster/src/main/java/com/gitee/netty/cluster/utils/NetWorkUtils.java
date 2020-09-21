package com.gitee.netty.cluster.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author jie
 */
public class NetWorkUtils {
    /**
     * 获取从1100开始的没有被占用的ip
     * @return
     */
    public static int getPort() {
        int initPort = 1100;
        while (true) {
            if (!isPortUsing(initPort)) {
                break;
            }
            initPort++;
        }
        return initPort;
    }

    /**
     * 检测端口是否被占用
     * @param port
     * @return
     */
    public static boolean isPortUsing(int port) {
        boolean flag = false;
        try {
            Socket socket = new Socket("localhost", port);
            socket.close();
            flag = true;
        } catch (IOException e) {

        }
        return flag;
    }

    /**
     * 杀掉端口进程
     * @param port
     */
    public static void killPort(int port){
        try {
            Runtime.getRuntime().exec("taskkill /F /pid " + port + "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取本机ip
     * @return
     * @throws UnknownHostException
     */
    public static String getHost() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }
}
