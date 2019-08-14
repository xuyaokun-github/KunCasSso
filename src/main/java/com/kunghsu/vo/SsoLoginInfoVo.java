package com.kunghsu.vo;

/**
 * 单点登录VO
 * 里面可以封装一些和登录用户相关的信息
 *
 */
public class SsoLoginInfoVo {

    private String username;
    private String password;

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
}
