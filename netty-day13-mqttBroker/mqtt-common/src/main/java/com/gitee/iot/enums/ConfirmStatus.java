package com.gitee.iot.enums;

/**
 * 枚举描述:状态确认
 * @author jie
 */
public enum ConfirmStatus {
    /**
     * 发布确认
     */
    PUB,
    /**
     * 发布收到(QOS2 第一步)
     */
    PUBREC,
    /**
     * 发布释放(QOS2 第二步)
     */
    PUBREL,
    /**
     * 发布完成(QOS2,第三部)
     */
    COMPLETE,
}
