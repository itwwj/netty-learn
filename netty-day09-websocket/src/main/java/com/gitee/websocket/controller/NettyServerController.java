package com.gitee.websocket.controller;

import com.gitee.websocket.model.InfoMsg;
import com.gitee.websocket.model.Result;
import com.gitee.websocket.utils.CacheUtil;
import com.gitee.websocket.utils.MsgBuild;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author jie
 */
@Slf4j
@RestController
public class NettyServerController {

    /**
     * 通过http向设备发消息
     *
     * @param reqStr
     * @return
     */

    @RequestMapping("/sendMsg")
    public Result sendMsg(String reqStr) {
        InfoMsg infoProtocol = MsgBuild.getMsg(reqStr);
        String channelId = infoProtocol.getChannelId();
        Channel channel = CacheUtil.cacheClientChannel.get(channelId);
        if (channel==null){
            return Result.eror("设备不存在");
        }
        channel.writeAndFlush(MsgBuild.buildMsg(infoProtocol));
        return Result.ok();
    }
}
