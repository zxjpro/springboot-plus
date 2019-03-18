package com.xiaojiezhu.springbootplus.cache;

import com.xiaojiezhu.cache.Cache;
import org.springframework.util.Assert;

/**
 * time 2019/3/18 14:11
 *
 * @author xiaojie.zhu <br>
 */
public class DefaultCacheProxy implements CacheProxy{
    private Cache cache;

    public DefaultCacheProxy(Cache cache) {
        this.cache = cache;
    }

    @Override
    public void set(String key, Object value, DataType dataType) {
        Assert.notNull(dataType , "dataType 不能为空");
        this.cache.set(key , value);
    }

    @Override
    public <T> T get(String key, DataType dataType) {
        Assert.notNull(dataType , "dataType 不能为空");
        Object value = null;
        switch (dataType){
            case INT:{
                value = this.cache.getInt(key);
                break;
            }
            case SHORT:{
                value = (short)((int)this.cache.getInt(key));
                break;
            }
            case LONG:{
                value = this.cache.getLong(key);
                break;
            }
            case FLOAT:{
                value = (float)((double)this.cache.getDouble(key));
                break;
            }
            case DOUBLE:{
                value = this.cache.getDouble(key);
                break;
            }
            case STRING:{
                value = this.cache.getString(key);
                break;
            }
            case DATE:{
                value = this.cache.getDate(key);
                break;
            }
            case JSON:{
                value = this.cache.getObject(key);
                break;
            }
            default:{
                throw new IllegalArgumentException("不支持的数据类型:" + dataType);
            }
        }
        return (T) value;
    }

    @Override
    public void expire(String key, long expireTimeMs) {
        this.cache.expire(key , expireTimeMs);
    }
}
