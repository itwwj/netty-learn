package com.gitee.websocket;

import com.gitee.websocket.model.Server;
import com.gitee.websocket.server.socket.SocketServer;
import com.gitee.websocket.server.websocket.WebsocketServer;
import com.gitee.websocket.utils.CacheUtil;
import com.gitee.websocket.utils.NetUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author jie
 */
@SpringBootApplication
public class Application implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }


    @Value("${netty.socket.port}")
    private int nettyServerPort;
    @Value("${netty.websocket.port}")
    private int nettyWsServerPort;

    private static ExecutorService executorService = Executors.newFixedThreadPool(2);

    @Override
    public void run(String... args) throws Exception {
        SocketServer websocketServer = new SocketServer(nettyServerPort);
        executorService.submit(websocketServer);
        CacheUtil.serverInfoMap.put(nettyServerPort, new Server("socket", NetUtil.getHost(), nettyServerPort, new Date()));

        WebsocketServer wsNettyServer = new WebsocketServer(nettyWsServerPort);
        executorService.submit(wsNettyServer);
        CacheUtil.serverInfoMap.put(nettyServerPort, new Server("websocket", NetUtil.getHost(), nettyServerPort, new Date()));
    }
}
