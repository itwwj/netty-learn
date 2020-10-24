package com.gitee.iot.pool;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.RejectedExecutionException;

/**
 * <p>LinkedTransferQueue 能保证更高性能，相比与LinkedBlockingQueue有明显提升
 * <p>不过LinkedTransferQueue的缺点是没有队列长度控制，需要在外层协助控制
 * @author jie
 */
public class ExecutorQueue extends LinkedTransferQueue<Runnable> {
    private static final long serialVersionUID = -265236426751004839L;
    private StandardThreadExecutor threadPoolExecutor;

    public ExecutorQueue() {
        super();
    }

    public void setStandardThreadExecutor(StandardThreadExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }

    /**
     * 注：代码来源于 tomcat
     * @param o
     * @return
     */
    public boolean force(Runnable o) {
        if (threadPoolExecutor.isShutdown()) {
            throw new RejectedExecutionException("Executor not running, can't force a command into the queue");
        }
        // forces the item onto the queue, to be used if the task is rejected
        return super.offer(o);
    }

    /**
     * 注：tomcat的代码进行一些小变更
     * @param o
     * @return
     */
    @Override
    public boolean offer(Runnable o) {
        int poolSize = threadPoolExecutor.getPoolSize();

        // we are maxed out on threads, simply queue the object
        if (poolSize == threadPoolExecutor.getMaximumPoolSize()) {
            return super.offer(o);
        }
        // we have idle threads, just add it to the queue
        // note that we don't use getActiveCount(), see BZ 49730
        if (poolSize >= threadPoolExecutor.getSubmittedTasksCount()) {
            return super.offer(o);
        }
        // if we have less threads than maximum force creation of a new
        // thread
        if (poolSize < threadPoolExecutor.getMaximumPoolSize()) {
            return false;
        }
        // if we reached here, we need to add it to the queue
        return super.offer(o);
    }
}
