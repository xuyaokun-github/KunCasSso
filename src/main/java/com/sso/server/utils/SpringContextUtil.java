package com.sso.server.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;


@Component
public class SpringContextUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    /**
     * 实现ApplicationContextAware接口的context注入函数, 将其存入静态变量.
     */
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

    /**
     * 取得存储在静态变量中的ApplicationContext.
     */
    public static ApplicationContext getContext() {
        checkApplicationContext();
        return applicationContext;
    }

    /**
     * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        checkApplicationContext();
        return (T) applicationContext.getBean(name);
    }

    /**
     * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz) {
        checkApplicationContext();
        return (T) applicationContext.getBeansOfType(clazz);
    }

    /**
     * 清除applicationContext静态变量.
     */
    public static void cleanApplicationContext() {
        applicationContext = null;
    }

    private static void checkApplicationContext() {
        Assert.notNull(applicationContext,
                "applicationContext未注入,请在applicationContext.xml中定义SpringContextUtil");
    }

    public static String getWEBINFPath(ApplicationContext applicationContext){

        String path = applicationContext.getClassLoader().getResource("").toString();
        if(System.getProperty("os.name").startsWith("Windows")){
            path=path.replace('/', '\\');
            path=path.replace("file:", "");
            path=path.replace("classes\\", "");
            path = path.substring(1);
        }else{
            path=path.replace("file:", "");
            path=path.replace("classes/", "");
        }

        return path;
    }

    public static String getWEBINFPath(){

        String path = getContext().getClassLoader().getResource("").toString();
        if(System.getProperty("os.name").startsWith("Windows")){
            path=path.replace('/', '\\');
            path=path.replace("file:", "");
            path=path.replace("classes\\", "");
            path = path.substring(1);
        }else{
            path=path.replace("file:", "");
            path=path.replace("classes/", "");
        }

        return path;
    }

}
