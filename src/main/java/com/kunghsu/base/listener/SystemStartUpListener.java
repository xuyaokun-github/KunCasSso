package com.kunghsu.base.listener;

import com.kunghsu.cache.CustomCacheManager;
import com.kunghsu.utils.JedisUtil;
import com.sso.server.utils.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

import static com.kunghsu.base.constants.CacheConstants.CACHESERVICE;
import static com.kunghsu.base.constants.CacheConstants.SESSION_CACHESERVICE;


/**
 * 系统启动完成监听器
 */
public class SystemStartUpListener implements ApplicationListener<ApplicationStartedEvent> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {

        String sessionCacheType = applicationStartedEvent.getApplicationContext()
                .getEnvironment().getProperty(SESSION_CACHESERVICE);
        String cacheType = applicationStartedEvent.getApplicationContext()
                .getEnvironment().getProperty(CACHESERVICE);

        CustomCacheManager.init(SpringContextUtil.getBean(sessionCacheType),
                SpringContextUtil.getBean(cacheType));

        JedisUtil.init("127.0.0.1", 6379, null);
    }


    
}
