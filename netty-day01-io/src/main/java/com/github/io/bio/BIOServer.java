package com.github.io.bio;


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
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        while (true) {
            final Socket accept = socket.accept();
            executorService.execute(() -> {
                InputStream is = null;
                OutputStream os = null;
                try {
                    is = accept.getInputStream();
                    StringBuilder message = new StringBuilder();
                    byte[] bytes = new byte[1024];
                    while (true) {
                        int read = is.read(bytes);
                        if (read != -1) {
                            message.append(new String(bytes));
                        } else {
                            System.out.println(message.toString());
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        is.close();
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}