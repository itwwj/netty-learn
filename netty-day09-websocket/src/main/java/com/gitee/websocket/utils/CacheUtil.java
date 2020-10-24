package com.gitee.websocket.utils;

import com.gitee.websocket.model.Device;
import com.gitee.websocket.model.Server;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * @author jie
 */
public class CacheUtil {

    /**
     * 用于存放用户Channel信息
     */
    public static ChannelGroup wsChannelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    /**
     * 缓存服务信息
     */
    public static Map<Integer, Server> serverInfoMap = Collections.synchronizedMap(new HashMap<>());
    /**
     * 缓存channelId -> Channel
     */
    public static Map<String, Channel> cacheClientChannel = Collections.synchronizedMap(new HashMap<>());
    /**
     *  设备
     */
    public static Map<String, Device> deviceGroup = Collections.synchronizedMap(new HashMap<>());
}
