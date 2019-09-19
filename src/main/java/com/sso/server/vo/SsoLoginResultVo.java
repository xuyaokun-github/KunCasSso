package com.sso.server.vo;

import java.io.Serializable;

public class SsoLoginResultVo implements Serializable {
    private static final long serialVersionUID = -1080136974200718581L;
    String errCode;
    String errMsg;
    String ssoSessionId;

    public SsoLoginResultVo() {
    }

    public static SsoLoginResultVo createSuccess(String ssoSessionId) {
        SsoLoginResultVo vo = new SsoLoginResultVo();
        vo.setErrCode("0");
        vo.setSsoSessionId(ssoSessionId);
        return vo;
    }

    public static SsoLoginResultVo createError(String errCode, String errMsg) {
        SsoLoginResultVo vo = new SsoLoginResultVo();
        vo.setErrCode(errCode);
        vo.setErrMsg(errMsg);
        return vo;
    }

    public static long getSerialVersionUID() {
        return -1080136974200718581L;
    }

    public String getErrCode() {
        return this.errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return this.errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getSsoSessionId() {
        return this.ssoSessionId;
    }

    public void setSsoSessionId(String ssoSessionId) {
        this.ssoSessionId = ssoSessionId;
    }
}

