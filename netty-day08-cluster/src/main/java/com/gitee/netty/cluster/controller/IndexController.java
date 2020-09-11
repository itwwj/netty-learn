package com.gitee.netty.cluster.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author jie
 */
@Controller
public class IndexController {

    @Value("${server.port}")
    private int serverPort;

    @RequestMapping("/index")
    public String index(Model model) {
        model.addAttribute("serverPort", serverPort);
        return "index";
    }
}
