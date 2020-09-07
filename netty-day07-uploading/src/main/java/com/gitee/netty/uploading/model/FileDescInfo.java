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

    private String fileUrl;
    private String fileName;
    private Long fileSize;

}
