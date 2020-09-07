package com.gitee.netty.uploading.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件分片数据
 * @author jie
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileBurstData {
    /**
     * 客户端文件地址
     */
    private String fileUrl;    
    /**
     * 文件名称
     */
    private String fileName;   
    /**
     * 开始位置
     */
    private Integer beginPos;  
    /**
     * 结束位置
     */
    private Integer endPos;    
    /**
     * 文件字节；再实际应用中可以使用非对称加密，以保证传输信息安全
     */
    private byte[] bytes;      
    /**
     * Constants.FileStatus ｛0开始、1中间、2结尾、3完成｝
     */
    private Integer status;
    public FileBurstData(Integer status){
        this.status = status;
    }

}
