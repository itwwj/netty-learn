package com.github.netty.bio;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * TODO
 *
 * @author jie
 * @date 2020/8/14 22:30
 */

public class BIOServer {
    public static void main(String[] args) throws IOException {

        ServerSocket socket = new ServerSocket(1100);
        System.out.println("开启服务");
        //创建线程池
        ExecutorService executorService = Executors.newCachedThreadPool();
        while (true) {
            final Socket accept = socket.accept();
            executorService.execute(new Thread(()-> {
                InputStream is=null;
                OutputStream os=null;
                    try {
                         is = accept.getInputStream();
                        byte[] bytes = new byte[1024];
                        int read = is.read(bytes);
                        System.out.println("接收到客户端消息："+new String(bytes, 0, read));
                         os = accept.getOutputStream();
                        os.write("好好学习，天天向上".getBytes());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        try {
                            is.close();
                            os.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            }));
        }
    }
}