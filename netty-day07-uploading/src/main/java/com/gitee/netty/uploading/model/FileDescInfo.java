package com.gitee.netty.uploading.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件描述信息
 * @author jie
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileDescInfo {
    /**
     * 文件地址
     */
    private String fileUrl;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件大小
     */
    private Long fileSize;

}
