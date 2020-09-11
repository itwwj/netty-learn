package com.gitee.netty.cluster.utils;

import com.gitee.netty.cluster.model.ServerInfo;
import com.gitee.netty.cluster.server.NettyServer;
import io.netty.channel.Channel;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * 本地缓存操作
 * @author jie
 */
public class CacheUtil {

    /**
     * 缓存channel
     */
    public static Map<String, Channel> cacheChannel = Collections.synchronizedMap(new HashMap<>());

    /**
     * 缓存服务信息
     */
    public static Map<Integer, ServerInfo> serverInfoMap = Collections.synchronizedMap(new HashMap<>());

    /**
     * 缓存服务端
     */
    public static Map<Integer, NettyServer> serverMap = Collections.synchronizedMap(new HashMap<>());

}
