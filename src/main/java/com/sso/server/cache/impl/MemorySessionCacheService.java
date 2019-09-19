package com.sso.server.cache.impl;

import com.sso.server.cache.ISessionCacheService;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class MemorySessionCacheService implements ISessionCacheService {

    public static ConcurrentHashMap<String, Object> sessiontList = new ConcurrentHashMap<>();

    @Override
    public void setSessionCache(String key, String value) {
        sessiontList.put(key, value);
    }

    @Override
    public <T> T getSessionCache(String key) {
        return (T) sessiontList.get(key);
    }

    @Override
    public void deleteSession(String key) {
        sessiontList.remove(key);
    }

}
