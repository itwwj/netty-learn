package com.gitee.netty.uploading.model;


/**
 *
 * 状态常量
 * @author jie
 */
public class Constants {

    /**
     * Constants.FileStatus ｛0开始、1中间、2结尾、3完成｝
     */
    public static class FileStatus {
        /**
         * 开始
         */
        public static int BEGIN = 0;
        /**
         * 中间
         */
        public static int CENTER = 1;
        /**
         * 结尾
         */
        public static int END = 2;
        /**
         * 完成
         */
        public static int COMPLETE = 3;
    }

    /**
     * 0传输文件'请求'、1文件传输'指令'、2文件传输'数据'
     */
    public static class TransferType {
        /**
         * 0请求传输文件
         */
        public static int REQUEST = 0;
        /**
         * 1文件传输指令
         */
        public static int INSTRUCT = 1;
        /**
         * 2文件传输数据
         */
        public static int DATA = 2;
    }

}
