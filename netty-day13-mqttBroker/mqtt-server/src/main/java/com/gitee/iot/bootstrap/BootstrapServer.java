package com.gitee.iot.bootstrap;


import com.gitee.iot.config.InitBean;

/**
 * <p>启动类接口
 * @author jie
 */
public interface BootstrapServer {
    /**
     * 关闭资源
     */
    void shutdown();

    /**
     * 初始化设置参数
     *
     * @param serverBean
     */
    void setServerBean(InitBean serverBean);

    /**
     * 服务开启
     */
    void start();
}
