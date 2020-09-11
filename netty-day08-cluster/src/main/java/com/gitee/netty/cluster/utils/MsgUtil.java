package com.gitee.netty.cluster.utils;


import com.alibaba.fastjson.JSON;
import com.gitee.netty.cluster.model.MsgAgreement;


/**
 * 构建消息
 * @author jie
 */
public class MsgUtil {

    public static MsgAgreement buildMsg(String channelId, String content) {
        return new MsgAgreement(channelId, content);
    }

    public static MsgAgreement json2Obj(String objJsonStr) {
        return JSON.parseObject(objJsonStr, MsgAgreement.class);
    }

    public static String obj2Json(MsgAgreement msgAgreement) {
        return JSON.toJSONString(msgAgreement) + "\r\n";
    }

}
