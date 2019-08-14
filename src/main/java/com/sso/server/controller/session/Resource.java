package com.sso.server.controller.session;

import org.springframework.stereotype.Component;

@Component
public class Resource {

    /** CACHE中保存SESSIONID时所加的前辍 **/
    public final static String QR_APP_SCAN = "QR_AppScan";

    /** 暂时未用到 **/
    public final static String QR_SYS_SID = "QR_SysSid";

    /** redis缓存区名称 **/
    public final static String WS_QUEUE_CACHE_NAME = "websocketCache";

    /** redis缓存区名称 **/
    public final static String AJAX_POLLING_CACHE_NAME = "ajaxPollingCache";

    public final static Object OBJECT2 = new Object();


}
