package com.gitee.netty.uploading.utils;


import com.gitee.netty.uploading.model.*;


/**
 * @author jie
 */
public class MsgUtil {

    /**
     * 构建对象；请求传输文件(客户端)
     *
     * @param fileUrl  客户端文件地址
     * @param fileName 文件名称
     * @param fileSize 文件大小
     * @return 传输协议
     */
    public static FileTransferProtocol buildRequestTransferFile(String fileUrl, String fileName, Long fileSize) {

        FileDescInfo fileDescInfo = new FileDescInfo();
        fileDescInfo.setFileUrl(fileUrl);
        fileDescInfo.setFileName(fileName);
        fileDescInfo.setFileSize(fileSize);

        FileTransferProtocol fileTransferProtocol = new FileTransferProtocol();
        ////0请求传输文件、1文件传输指令、2文件传输数据
        fileTransferProtocol.setTransferType(0);
        fileTransferProtocol.setTransferObj(fileDescInfo);

        return fileTransferProtocol;

    }

    /**
     * 构建对象；文件传输指令(服务端)
     * @param status          0请求传输文件、1文件传输指令、2文件传输数据
     * @param clientFileUrl   客户端文件地址
     * @param readPosition    读取位置
     * @return                传输协议
     */
    public static FileTransferProtocol buildTransferInstruct(Integer status, String clientFileUrl, Integer readPosition) {

        FileBurstInstruct fileBurstInstruct = new FileBurstInstruct();
        fileBurstInstruct.setStatus(status);
        fileBurstInstruct.setClientFileUrl(clientFileUrl);
        fileBurstInstruct.setReadPosition(readPosition);

        FileTransferProtocol fileTransferProtocol = new FileTransferProtocol();
        //0传输文件'请求'、1文件传输'指令'、2文件传输'数据'
        fileTransferProtocol.setTransferType(Constants.TransferType.INSTRUCT);
        fileTransferProtocol.setTransferObj(fileBurstInstruct);

        return fileTransferProtocol;
    }

    /**
     * 构建对象；文件传输指令(服务端)
     *
     * @return 传输协议
     */
    public static FileTransferProtocol buildTransferInstruct(FileBurstInstruct fileBurstInstruct) {
        FileTransferProtocol fileTransferProtocol = new FileTransferProtocol();
        //0传输文件'请求'、1文件传输'指令'、2文件传输'数据'
        fileTransferProtocol.setTransferType(Constants.TransferType.INSTRUCT);
        fileTransferProtocol.setTransferObj(fileBurstInstruct);
        return fileTransferProtocol;
    }

    /**
     * 构建对象；文件传输数据(客户端)
     *
     * @return 传输协议
     */
    public static FileTransferProtocol buildTransferData(FileBurstData fileBurstData) {
        FileTransferProtocol fileTransferProtocol = new FileTransferProtocol();
        //0传输文件'请求'、1文件传输'指令'、2文件传输'数据'
        fileTransferProtocol.setTransferType(Constants.TransferType.DATA);
        fileTransferProtocol.setTransferObj(fileBurstData);
        return fileTransferProtocol;
    }

}
