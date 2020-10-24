package com.gitee.iot.bootstrap.channel;


import com.gitee.iot.bootstrap.BaseApi;
import com.gitee.iot.bootstrap.ChannelService;
import com.gitee.iot.bootstrap.bean.WillMeaasge;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>遗嘱消息处理
 * @author jie
 */
@Service
@Slf4j
@Data
@NoArgsConstructor
public class WillService implements BaseApi {



    @Autowired
    ChannelService channelService;
    /**
     *  deviceid -WillMeaasge
     */
    private static ConcurrentHashMap<String, WillMeaasge> willMeaasges = new ConcurrentHashMap<>();


    /**
     * 保存遗嘱消息
     */
    public void save(String deviceid, WillMeaasge build) {
        willMeaasges.put(deviceid, build);
    }

    /**
     *   客户端断开连接后 开启遗嘱消息发送
     * @param deviceId
     */
    public void doSend(String deviceId) {
        if (StringUtils.isNotBlank(deviceId) && (willMeaasges.get(deviceId)) != null) {
            WillMeaasge willMeaasge = willMeaasges.get(deviceId);
            // 发送遗嘱消息
            channelService.sendWillMsg(willMeaasge);
            // 移除
            if (!willMeaasge.isRetain()) {
                willMeaasges.remove(deviceId);
                log.info("deviceId will message[" + willMeaasge.getWillMessage() + "] is removed");
            }
        }
    }

    /**
     * 删除遗嘱消息
     */
    public void del(String deviceid) {
        willMeaasges.remove(deviceid);
    }
}
