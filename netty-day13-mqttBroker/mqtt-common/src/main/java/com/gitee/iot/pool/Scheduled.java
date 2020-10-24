package com.gitee.iot.pool;

import java.util.concurrent.ScheduledFuture;

/**
 * @author jie
 */
@FunctionalInterface
public interface Scheduled {
    ScheduledFuture<?> submit(Runnable runnable);
}
