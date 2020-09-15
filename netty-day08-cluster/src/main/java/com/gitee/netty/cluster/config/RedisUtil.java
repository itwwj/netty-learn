package com.gitee.netty.cluster.config;

import com.alibaba.fastjson.JSON;
import com.gitee.netty.cluster.model.DeviceChannelInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * @author jie
 */
@Service
public class RedisUtil {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 向redis存入设备连接信息
     * @param deviceChannelInfo
     */
    public void pushObj(DeviceChannelInfo deviceChannelInfo) {
        redisTemplate.opsForHash().put("deviceIds", deviceChannelInfo.getChannelId(), JSON.toJSONString(deviceChannelInfo));
    }

    /**
     * 查询redis中的设备连接信息
     * @return
     */
    public List<DeviceChannelInfo> popList() {
        List<Object> values = redisTemplate.opsForHash().values("deviceIds");
        if (null == values) {
            return new ArrayList<>();
        }
        List<DeviceChannelInfo> deviceChannelInfoList = new ArrayList<>();
        for (Object strJson : values) {
            deviceChannelInfoList.add(JSON.parseObject(strJson.toString(), DeviceChannelInfo.class));
        }
        return deviceChannelInfoList;
    }

    /**
     * 移除某个设备信息
     * @param channelId
     */
    public void remove(String channelId) {
        redisTemplate.opsForHash().delete("deviceIds", channelId);
    }

    /**
     * 清空设备信息
     */
    public void clear() {
        redisTemplate.delete("deviceIds");
    }
}
