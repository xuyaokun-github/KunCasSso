package com.sso.server.cache.impl;

import com.sso.server.cache.IObjectCacheService;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 自定义的基于内存的缓存服务类
 * 用于单节点模拟用，实际生产上推荐使用Redis
 *
 * Created by xuyaokun On 2019/8/15 21:18
 * @desc:
 */
@Component
public class MemoryCacheService implements IObjectCacheService{

    public static ConcurrentHashMap<String, Object> objectList = new ConcurrentHashMap<>();

    @Override
    public void setCache(String key, Object value, String expiresTime) {
        objectList.put(key, value);
    }

    @Override
    public Object getCache(String key) {

        return objectList.get(key);
    }

    @Override
    public <T> T getCache(String key, Class<T> clazz) {
        return (T) objectList.get(key);
    }

    @Override
    public void delete(String key) {
        objectList.remove(key);
    }

}
