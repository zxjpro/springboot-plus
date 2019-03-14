package com.xiaojiezhu.springbootplus.lock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * time 2019/3/14 17:34
 *
 * @author xiaojie.zhu <br>
 */
public class LockFactory {

    private RedissonClient redissonClient;
    private ConcurrentHashMap<String,Lock> locks = new ConcurrentHashMap<>();

    public LockFactory(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }


    public Lock newLock(String lockString , long lockTime){
        if(redissonClient == null){
            Lock lock = locks.get(lockString);
            if(lock == null){
                synchronized (this){
                    lock = locks.get(lockString);
                    if(lock == null){
                        lock = new ReentrantLock();
                        locks.put(lockString , lock);
                    }
                }
            }
            return lock;


        }else{
            // 分布式锁
            RLock lock = redissonClient.getLock(lockString);
            lock.lock();
            return lock;
        }
    }


}
