package com.gitee.netty.cluster.controller;

import com.alibaba.fastjson.JSON;
import com.gitee.netty.cluster.config.RedisUtil;
import com.gitee.netty.cluster.model.DeviceChannelInfo;
import com.gitee.netty.cluster.model.ServerInfo;
import com.gitee.netty.cluster.utils.CacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collection;
import java.util.List;

/**
 * @author jie
 */
@Slf4j
@RestController
public class NettyController {

    @Autowired
    private RedisUtil redisUtil;


    @RequestMapping("/queryNettyServerList")
    public Collection<ServerInfo> queryNettyServerList() {
        try {
            Collection<ServerInfo> serverInfos = CacheUtil.serverInfoMap.values();
            log.info("查询服务端列表。{}", JSON.toJSONString(serverInfos));
            return serverInfos;
        } catch (Exception e) {
            log.info("查询服务端列表失败。", e);
            return null;
        }
    }

    @RequestMapping("/queryDeviceChannelInfoList")
    public List<DeviceChannelInfo> queryUserChannelInfoList() {
        try {
            log.info("查询设备列表信息开始");
            List<DeviceChannelInfo> userChannelInfoList = redisUtil.popList();
            log.info("查询设备列表信息完成。list：{}", JSON.toJSONString(userChannelInfoList));
            return userChannelInfoList;
        } catch (Exception e) {
            log.error("查询设备列表信息失败", e);
            return null;
        }
    }
}
