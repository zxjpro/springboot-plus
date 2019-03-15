package com.xiaojiezhu.springbootplus.lock;

import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

/**
 * time 2019/3/15 9:27
 *
 * @author xiaojie.zhu <br>
 */
class PRLock implements DLock {

    private RLock rLock;

    public PRLock(RLock rLock) {
        this.rLock = rLock;
    }

    @Override
    public void lock() {
        rLock.lock();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        rLock.lockInterruptibly();
    }

    @Override
    public boolean tryLock() {
        return rLock.tryLock();
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return rLock.tryLock(time, unit);
    }

    @Override
    public void unlock() {
        rLock.unlock();
    }

    @Override
    public Condition newCondition() {
        return rLock.newCondition();
    }

    @Override
    public void lockInterruptibly(long leaseTime, TimeUnit unit) throws InterruptedException {
        rLock.lockInterruptibly(leaseTime , unit);
    }
}
