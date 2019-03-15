package com.xiaojiezhu.springbootplus.lock;

import com.xiaojiezhu.cache.Cache;
import com.xiaojiezhu.cache.MemoryCache;
import org.redisson.api.RedissonClient;

import java.util.concurrent.locks.ReentrantLock;

/**
 * time 2019/3/14 17:34
 *
 * @author xiaojie.zhu <br>
 */
public class LockFactory {

    private RedissonClient redissonClient;
    private final Cache cache = new MemoryCache();

    public LockFactory(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }


    public DLock newLock(String lockString , long timeout){
        if(redissonClient != null){
            // 分布式锁
            DLock dLock = new PRLock(redissonClient.getLock(lockString));
            return dLock;
        }else{
            // java 锁
            DLock dLock = cache.getObject(lockString);
            if(dLock == null){
                synchronized (lockString){
                    dLock = cache.getObject(lockString);
                    if(dLock == null){
                        dLock = new JavaLock(new ReentrantLock());
                        cache.set(lockString , dLock);
                        cache.expire(lockString , timeout + 5000);
                    }
                }
            }
            return dLock;

        }



    }


}
