package com.gitee.netty.cluster.controller;

import com.alibaba.fastjson.JSON;
import com.gitee.netty.cluster.config.RedisUtil;
import com.gitee.netty.cluster.model.DeviceChannelInfo;
import com.gitee.netty.cluster.model.ServerInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author jie
 */
@Controller
public class IndexController {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @RequestMapping("/index")
    public String index(Model model) {

        Set<String> keys = redisTemplate.keys("*");
        List<ServerInfo> values = new ArrayList<>();
        for (String key : keys) {
            if (key.contains("server")) {
                values.add(JSON.parseObject(redisTemplate.boundValueOps(key).get().toString(), ServerInfo.class));
            }
        }
        List<DeviceChannelInfo> ChannelInfoList = redisUtil.popList();

        model.addAttribute("server", values);
        model.addAttribute("device", ChannelInfoList);
        return "index";
    }
}
