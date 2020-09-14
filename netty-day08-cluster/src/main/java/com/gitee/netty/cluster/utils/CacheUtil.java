package com.gitee.netty.cluster.utils;

import io.netty.channel.Channel;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author jie
 */
public class CacheUtil {
    /**
     * 线程池
     */
    public static ExecutorService executorService = Executors.newFixedThreadPool(3);
    /**
     * 缓存channel
     */
    public static Map<String, Channel> cacheChannel = Collections.synchronizedMap(new HashMap<>());
}
