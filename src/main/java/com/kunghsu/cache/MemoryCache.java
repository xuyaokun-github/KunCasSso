package com.kunghsu.cache;

import java.util.concurrent.ConcurrentHashMap;

public class MemoryCache {

    public static ConcurrentHashMap<String, Object> objectList = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, Object> sessiontList = new ConcurrentHashMap<>();


    public static void setCache(String key, Object value, String expiresTime){
        objectList.put(key, value);
    }

    public static Object getCache(String key){
        return objectList.get(key);
    }


    public static void delete(String key){
        objectList.remove(key);
    }


    public static void setSessionCache(String key, Object value){
        sessiontList.put(key, value);
    }

    public static Object getSessionCache(String key){
        return sessiontList.get(key);
    }


    public static void deleteSession(String key){
        sessiontList.remove(key);
    }
}
