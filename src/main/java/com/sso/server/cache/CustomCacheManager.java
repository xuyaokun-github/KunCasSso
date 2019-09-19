package com.sso.server.cache;

/**
 * 自定义的缓存处理器
 * Created by xuyaokun On 2019/8/14 22:36
 * @desc:
 */
public class CustomCacheManager {

    private static ISessionCacheService sessionCacheService;
    private static IObjectCacheService objectCacheService;

    /**
     * 初始化
     * @param scs
     * @param ocs
     */
    public static void init(ISessionCacheService scs, IObjectCacheService ocs){
        sessionCacheService = scs;
        objectCacheService = ocs;
    }

    public static <T> void setCache(String key, T value, String expiresTime){
        objectCacheService.setCache(key, value, expiresTime);
    }

    public static <T> T getCache(String key){
        return objectCacheService.getCache(key);

    }

    public static <T> T getCache(String key, Class<T> clazz){
        return objectCacheService.getCache(key, clazz);
    }

    public static void delete(String key){
        objectCacheService.delete(key);
    }


    public static void setSessionCache(String key, String value){
        sessionCacheService.setSessionCache(key, value);
    }

    public static <T> T getSessionCache(String key){
        return sessionCacheService.getSessionCache(key);

    }


    public static void deleteSession(String key){
        sessionCacheService.deleteSession(key);
    }

}
