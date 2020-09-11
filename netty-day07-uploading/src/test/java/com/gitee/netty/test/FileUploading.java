package com.gitee.netty.test;

import com.gitee.netty.uploading.client.NettyClient;
import com.gitee.netty.uploading.model.FileTransferProtocol;
import com.gitee.netty.uploading.utils.MsgUtil;
import io.netty.channel.ChannelFuture;

import java.io.File;

/**
 * @author jie
 */
public class FileUploading {
    public static void main(String[] args) {
        ChannelFuture channelFuture = new NettyClient().init("127.0.0.1", 1100);
        File file=new File("D:\\360MoveData\\Users\\jie\\Desktop\\Netty权威指南第2版.pdf");
        FileTransferProtocol fileTransferProtocol = MsgUtil.buildRequestTransferFile(file.getAbsolutePath(), file.getName(), file.length());
        channelFuture.channel().writeAndFlush(fileTransferProtocol);
    }
}
