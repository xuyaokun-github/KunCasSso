package com.sso.server.vo;

import java.util.HashMap;
import java.util.Map;

/**
 * 单点登录VO
 * 里面可以封装一些和登录用户相关的信息
 *
 */
public class SsoLoginInfoVo {

    //所有和业务相关的信息都可以封装到这个VO里
    //必须要有setter/getter方法
    private String username;
    private String password;
    private Map<String, String> others = new HashMap<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void add(String key, String value){
        this.others.put(key, value);
    }

    public Map<String, String> getOthers() {
        return others;
    }

    public void setOthers(Map<String, String> others) {
        this.others = others;
    }
}
