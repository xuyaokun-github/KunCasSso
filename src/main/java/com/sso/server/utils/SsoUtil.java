package com.sso.server.utils;

import com.alibaba.fastjson.JSONObject;
import com.kunghsu.cache.CustomCacheManager;
import com.kunghsu.vo.SessionVO;
import com.kunghsu.vo.SsoLoginInfoVo;
import org.jasig.cas.client.validation.Assertion;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class SsoUtil {

    public static String SSO_SESSION_KEY = "loginInfo";

    public static SsoLoginInfoVo getSsoLoginInfo(HttpServletRequest request) {
        SsoLoginInfoVo ssoLoginInfoVo = (SsoLoginInfoVo)request.getSession().getAttribute("loginInfo");
        if (ssoLoginInfoVo == null) {
            Assertion assertion = (Assertion)request.getSession().getAttribute("_const_cas_assertion_");
            if (assertion != null) {
                try {
                    ssoLoginInfoVo = (SsoLoginInfoVo) JSONObject.parseObject(assertion.getPrincipal().getName(), SsoLoginInfoVo.class);
                    request.getSession().setAttribute("loginInfo", ssoLoginInfoVo);
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
     * @param sessionType
     * @return
     */
    public static String createSession(String clientSessionId, SsoLoginInfoVo loginInfo, String sessionType) {
        SessionVO sessionVo = null;

        if (clientSessionId != null && !"".equals(clientSessionId)) {
            //根据sessionID获取session

            //以前的设计是从会话中心去获取sessionID
//            ISessionService sessionService = BondeInterfaceFactory.getInstance().getInterface(ISessionService.class);
//            String sessionStr = sessionService.getSession(clientSessionId);

            //TODO 可以从redis中取session，后续的优化

            //模拟
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
        sessionVo.setAttribute(SsoUtil.SSO_SESSION_KEY, loginInfo);

        //之前的设计
//        ISessionService sessionService = BondeInterfaceFactory.getInstance().getInterface(ISessionService.class);
//        sessionService.setSession(sessionVo.toJson());

        //模拟
        CustomCacheManager.setSessionCache(clientSessionId, sessionVo.toJson());

        return sessionVo.getId();
    }


}
