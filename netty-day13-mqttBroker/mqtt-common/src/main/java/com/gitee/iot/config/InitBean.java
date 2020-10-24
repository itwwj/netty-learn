package com.gitee.iot.config;


import com.gitee.iot.enums.ProtocolEnum;
import com.gitee.iot.mqtt.AbstractMqttHander;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p> netty服务端启动参数
 * @author jie
 */
@ConfigurationProperties(prefix = "netty.mqtt.server")
@Data
public class InitBean {
    /**
     * 协议类型
     */
    private ProtocolEnum protocol;
    /**
     * 端口号
     */
    private int port;
    /**
     * 服务名
     */
    private String serverName;
    /**
     * 连接保活  心跳
     */
    private boolean keepalive;
    /**
     * 地址复用，netty默认值False。
     * 有四种情况可以使用：
     * (1).当有一个有相同本地地址和端口的socket1处于TIME_WAIT状态时，
     * 而你希望启动的程序的socket2要占用该地址和端口，比如重启服务且保持先前端口。
     * (2).有多块网卡或用IP Alias技术的机器在同一端口启动多个进程，但每个进程绑定的本地IP地址不能相同。
     * (3).单个进程绑定相同的端口到多个socket上，但每个socket绑定的ip地址不同。
     * (4).完全相同的地址和端口的重复绑定。但这只用于UDP的多播，不用于TCP。
     */
    private boolean reuseaddr;
    /**
     * TCP参数，立即发送数据，默认值为Ture（Netty默认为True而操作系统默认为False）。
     * 该值设置Nagle算法的启用，改算法将小的碎片数据连接成更大的报文来最小化所发送的报文的数量，
     * 如果需要发送一些较小的报文，则需要禁用该算法。Netty默认禁用该算法，从而最小化报文传输延时。
     */
    private boolean tcpNodelay;
    /**
     * Socket参数，服务端接受连接的队列长度，如果队列已满，客户端连接将被拒绝
     */
    private int backlog;
    /**
     * 读超时时间
     */
    private int heart;
    /**
     * 是否使用ssl加密
     */
    private boolean ssl;
    /**
     * ssl 加密 jks文件地址
     */
    private String jksFile;
    /**
     * 读取jks密码
     */
    private String jksStorePassword;
    /**
     * 读取私钥密码
     */
    private String jksCertificatePassword;
    /**
     * 默认处理hander
     */
    private Class<AbstractMqttHander> mqttHander;

    /**
     * mqtt qos1 qos2 消息 重发延迟
     */
    private int initalDelay;
    /**
     * mqtt qos1 qos2 消息 重发周期
     */
    private int period;


    private int sndbuf;

    private int revbuf;

    private int bossThread;

    private int workThread;
}
