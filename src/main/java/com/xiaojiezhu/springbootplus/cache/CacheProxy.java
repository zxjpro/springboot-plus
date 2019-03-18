package com.xiaojiezhu.springbootplus.cache;

/**
 * time 2019/3/18 14:09
 *
 * @author xiaojie.zhu <br>
 */
public interface CacheProxy {

    /**
     * 设置一个值
     * @param key
     * @param value
     * @param dataType
     */
    void set(String key , Object value , DataType dataType);

    /**
     * 获取一个值
     * @param key
     * @param dataType
     * @param <T>
     * @return
     */
    <T> T get(String key , DataType dataType);


    /**
     * 设置过期时间，
     * @param key key
     * @param expireTimeMs 过期时间，毫秒
     */
    void expire(String key , long expireTimeMs);
}
