package com.kunghsu.cache;

public interface IObjectCacheService {

    void setCache(String key, Object value, String expiresTime);

    <T> T getCache(String key, Class<T> clazz);

    <T> T getCache(String key);

    void delete(String key);

}
