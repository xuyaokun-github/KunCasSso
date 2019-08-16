package com.kunghsu.cache.impl;

import com.kunghsu.cache.ISessionCacheService;
import com.kunghsu.utils.RedissonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * 基于Redis的会话缓存服务类
 * 会话存在Redis中，为了分布式场景，服务端可以部署多个节点
 *
 * Created by xuyaokun On 2019/8/15 21:20
 * @desc:
 */
@Component
public class RedisSessionCacheService implements ISessionCacheService{

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static long SESSION_TIMEOUT = 60 * 30;//会话超时：30分钟

    @Override
    public void setSessionCache(String key, String value) {
        logger.debug("添加会话：" + key);
        RedissonUtil.setString(key, value, SESSION_TIMEOUT);
    }

    @Override
    public <T> T getSessionCache(String key) {
        logger.debug("获取会话：" + key);
        return (T) RedissonUtil.getString(key);
    }

    @Override
    public void deleteSession(String key) {
        logger.debug("删除会话：" + key);
        RedissonUtil.delete(key);
    }
}
