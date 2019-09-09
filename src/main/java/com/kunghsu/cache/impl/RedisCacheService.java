package com.kunghsu.cache.impl;

import com.kunghsu.cache.IObjectCacheService;
import com.kunghsu.utils.RedissonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class RedisCacheService implements IObjectCacheService{


    @Override
    public void setCache(String key, Object value, String expiresTime) {

        if (StringUtils.isEmpty(expiresTime)){
            RedissonUtil.setObject(key, value);
        }else {
            RedissonUtil.setObject(key, value, Long.valueOf(expiresTime));
        }

        //基于Jedis操作
//        if (StringUtils.isEmpty(expiresTime)) {
//            JedisUtil.getInstance().setExByte(key, value, 0);
//        } else {
//            JedisUtil.getInstance().setExByte(key, value, Integer.parseInt(expiresTime));
//        }
    }

    @Override
    public Object getCache(String key) {
        return RedissonUtil.getObject(key);
        //基于Jedis操作
//        return JedisUtil.getInstance().getByteObject(key);
    }

    @Override
    public <T> T getCache(String key, Class<T> clazz) {
        return null;
    }

    @Override
    public void delete(String key) {
        RedissonUtil.delete(key);
    }
}
