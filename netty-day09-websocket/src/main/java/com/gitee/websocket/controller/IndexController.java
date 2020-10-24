package com.gitee.websocket.controller;

import com.gitee.websocket.model.Device;
import com.gitee.websocket.model.Server;
import com.gitee.websocket.utils.CacheUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

/**
 * @author jie
 */
@Controller
public class IndexController {

    @RequestMapping("index")
    public String index(Model model) {
        Collection<Server> servers = CacheUtil.serverInfoMap.values();
        Collection<Device> devices = CacheUtil.deviceGroup.values();
        model.addAttribute("services",servers);
        model.addAttribute("devices",devices);
        return "index";
    }
}
