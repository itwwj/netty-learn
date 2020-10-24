package com.gitee.iot.auto;

import com.gitee.iot.config.InitBean;
import com.gitee.iot.enums.ProtocolEnum;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p> 自动化配置初始化服务
 * @author jie
 */

@Configuration
@ConditionalOnClass
@EnableConfigurationProperties({InitBean.class})
public class ServerAutoConfigure {

    /**
     * 服务端接受连接的队列长度
     */
    private static final int _BLACKLOG = 1024;

    private static final int CPU = Runtime.getRuntime().availableProcessors();
    /**
     * mqtt qos1 qos2 消息 重发延迟 周期
     */
    private static final int SEDU_DAY = 10;
    /**
     * 读超时时间
     */
    private static final int TIMEOUT = 120;
    /**
     * 接收buf大小
     */
    private static final int BUF_SIZE = 10 * 1024 * 1024;


    @Bean(initMethod = "open", destroyMethod = "close")
    @ConditionalOnMissingBean
    public InitServer initServer(InitBean serverBean) {
        if (!ObjectUtils.allNotNull(serverBean.getPort(), serverBean.getServerName())) {
            throw new NullPointerException("not set port");
        }
        if (serverBean.getBacklog() < 1) {
            serverBean.setBacklog(_BLACKLOG);
        }
        if (serverBean.getBossThread() < 1) {
            serverBean.setBossThread(CPU);
        }
        if (serverBean.getInitalDelay() < 0) {
            serverBean.setInitalDelay(SEDU_DAY);
        }
        if (serverBean.getPeriod() < 1) {
            serverBean.setPeriod(SEDU_DAY);
        }
        if (serverBean.getHeart() < 1) {
            serverBean.setHeart(TIMEOUT);
        }
        if (serverBean.getRevbuf() < 1) {
            serverBean.setRevbuf(BUF_SIZE);
        }
        if (serverBean.getWorkThread() < 1) {
            serverBean.setWorkThread(CPU * 2);
        }
        if (serverBean.getProtocol() == null) {
            serverBean.setProtocol(ProtocolEnum.MQTT);
        }
        return new InitServer(serverBean);
    }

}
