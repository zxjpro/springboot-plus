package com.xiaojiezhu.springbootplus.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * time 2019/3/15 9:24
 *
 * @author xiaojie.zhu <br>
 */
public interface DLock extends Lock {

    /**
     * 获取一个锁资源，如果获取了锁，它将一直持有，直到调用unlock释放锁，或者到了leaseTime，锁过期
     * 在分布式锁中必用，可以避免像redis宕机，应用程序挂掉导致无限的死锁
     * @param leaseTime
     * @param unit
     * @throws InterruptedException
     */
    void lockInterruptibly(long leaseTime, TimeUnit unit) throws InterruptedException;
}
