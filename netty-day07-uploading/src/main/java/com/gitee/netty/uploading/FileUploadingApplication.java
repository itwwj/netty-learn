package com.gitee.netty.uploading;

import com.gitee.netty.uploading.server.NettyServer;
import io.netty.channel.ChannelFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author jie
 */
@SpringBootApplication
public class FileUploadingApplication implements CommandLineRunner {


    public static void main(String[] args) {
        SpringApplication.run(FileUploadingApplication.class, args);
    }

    @Value("${netty.port}")
    private int port;
    @Autowired
    private NettyServer nettyServer;

    @Override
    public void run(String... args) throws Exception {
        ChannelFuture init = nettyServer.init(port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> nettyServer.close()));
        init.channel().closeFuture().syncUninterruptibly();
    }
}
