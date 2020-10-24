package com.gitee.iot.auto;


import com.gitee.iot.bootstrap.BootstrapServer;
import com.gitee.iot.bootstrap.NettyBootstrapServer;
import com.gitee.iot.config.InitBean;

/**
 * <p>初始化服务
 * @author jie
 */
public class InitServer {
    private InitBean serverBean;

    public InitServer(InitBean serverBean) {
        this.serverBean = serverBean;
    }

    BootstrapServer bootstrapServer;

    public void open(){
        if(serverBean!=null){
            bootstrapServer = new NettyBootstrapServer();
            bootstrapServer.setServerBean(serverBean);
            bootstrapServer.start();
        }
    }


    public void close(){
        if(bootstrapServer!=null){
            bootstrapServer.shutdown();
        }
    }
}
