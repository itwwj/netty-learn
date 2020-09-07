package com.gitee.netty.boot.controller;

import com.gitee.netty.boot.server.NettyServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jie
 */
@Slf4j
@RestController
@RequestMapping("/netty")
public class NettyController {

    @Autowired
    private NettyServer nettyServer;

    @RequestMapping("/localAddress")
    public String localAddress() {
        return "tcp服务端地址：" + nettyServer.getChannel().localAddress();
    }

    @RequestMapping("/isOpen")
    public String isOpen() {
        if (nettyServer.getChannel().isOpen()){
            return "tcp服务已启动";
        }else {
            return "tcp服务未启动";
        }
    }

/*    @RequestMapping("/open")
    public String open(int port) {
        if (nettyServer.getChannel().isOpen()) {
            return "服务端已启动，请勿重复启动";
        }
        ChannelFuture init = nettyServer.init(port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> nettyServer.close()));
        init.channel().closeFuture().syncUninterruptibly();
        return "服务端启动成功";
    }*/

    @RequestMapping("/close")
    public String close() {
        if (nettyServer.getChannel().isOpen()) {
            nettyServer.close();
        }
        return "服务端关闭成功";
    }

}
