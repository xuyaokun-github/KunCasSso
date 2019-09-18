package com.sso.server.utils;

import com.alibaba.fastjson.JSONObject;
import com.kunghsu.cache.CustomCacheManager;
import com.kunghsu.vo.SessionVO;
import com.kunghsu.vo.SsoLoginInfoVo;
import org.jasig.cas.client.validation.Assertion;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static com.sso.server.constant.SsoContant.SSO_SESSION_KEY;

public class SsoUtil {

    public static SsoLoginInfoVo getSsoLoginInfo(HttpServletRequest request) {
        SsoLoginInfoVo ssoLoginInfoVo = (SsoLoginInfoVo)request.getSession().getAttribute("loginInfo");
        if (ssoLoginInfoVo == null) {
            Assertion assertion = (Assertion)request.getSession().getAttribute("_const_cas_assertion_");
            if (assertion != null) {
                try {
                    ssoLoginInfoVo = (SsoLoginInfoVo) JSONObject.parseObject(assertion.getPrincipal().getName(), SsoLoginInfoVo.class);
                    request.getSession().setAttribute(SSO_SESSION_KEY, ssoLoginInfoVo);
                } catch (Exception var4) {
                    throw new RuntimeException(var4);
                }
            }
        }

        return ssoLoginInfoVo;
    }

    /**
     * 创建session
     *
     * @param clientSessionId
     * @param loginInfo
     * @param sessionType（自定义session类型，不仅仅是30分钟，可扩展各种时长）
     * @return
     */
    public static String createSession(String clientSessionId, SsoLoginInfoVo loginInfo, String sessionType) {
        SessionVO sessionVo = null;

        if (clientSessionId != null && !"".equals(clientSessionId)) {
            //根据sessionID获取session
            //以前的设计是从会话中心去获取sessionID，现在改成从redis中获取

            //从redis中取session，后续的优化
            String sessionStr = (String) CustomCacheManager.getSessionCache(clientSessionId);
            sessionVo = JSONObject.parseObject(sessionStr, SessionVO.class);
        }
        if (sessionVo == null) {
            sessionVo = new SessionVO();
            // 初始化session
            sessionVo.setCreateTime(new Date().getTime());
        }

        //自定义的规则
        if ("1".equals(sessionType)) {
            sessionVo.setMaxInactiveInterval(60 * 60 * 24 * 7);
        }
        sessionVo.setAttribute(SSO_SESSION_KEY, loginInfo);

        CustomCacheManager.setSessionCache(clientSessionId, sessionVo.toJson());

        return sessionVo.getId();
    }


}
