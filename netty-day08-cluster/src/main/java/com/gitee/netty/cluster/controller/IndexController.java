package com.gitee.netty.cluster.controller;

import com.gitee.netty.cluster.config.RedisUtil;
import com.gitee.netty.cluster.model.DeviceChannelInfo;
import com.gitee.netty.cluster.model.ServerInfo;
import com.gitee.netty.cluster.utils.CacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;
import java.util.List;

/**
 * @author jie
 */
@Controller
public class IndexController {

    @Autowired
    private RedisUtil redisUtil;


    @RequestMapping("/index")
    public String index(Model model) {

        Collection<ServerInfo> values = CacheUtil.serverInfoMap.values();
        List<DeviceChannelInfo> ChannelInfoList = redisUtil.popList();

        model.addAttribute("server",values);
        model.addAttribute("device",ChannelInfoList);
        return "index";
    }
}
