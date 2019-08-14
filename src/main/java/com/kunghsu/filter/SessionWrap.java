package com.kunghsu.filter;


import com.alibaba.fastjson.JSONObject;
import com.kunghsu.cache.MemoryCache;
import com.kunghsu.vo.SessionVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.*;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

public class SessionWrap implements HttpSession {
    private final Logger logger = LoggerFactory.getLogger(SessionWrap.class);
    private String sessionCookieName;
    private HttpSession session;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private String sessionId = null;
    private boolean isHttpRequest = true;
    private String cookieDomain;
    private int maxInactiveInterval = 1800;

    public SessionWrap(HttpSession session, HttpServletRequest request, HttpServletResponse response, String sessionCookieName, String cookieDomain) {
        this.session = session;
        this.request = request;
        this.response = response;
        this.sessionCookieName = sessionCookieName;
        this.cookieDomain = cookieDomain;
        this.maxInactiveInterval = session.getMaxInactiveInterval();
    }

    public SessionWrap(String sessionId, int maxInactiveInterval) {
        this.isHttpRequest = false;
        this.sessionId = sessionId;
        this.maxInactiveInterval = this.session.getMaxInactiveInterval();
    }

    public Enumeration<String> getAttributeNames() {
        SessionVO sessionVo = this.getSessionVo();
        if (sessionVo == null) {
            return null;
        } else {
            Vector<String> keys = new Vector();
            Iterator i$ = sessionVo.getAttributes().keySet().iterator();

            while(i$.hasNext()) {
                String key = (String)i$.next();
                keys.add(key);
            }

            return keys.elements();
        }
    }

    public long getCreationTime() {
        SessionVO sessionVo = this.getSessionVo();
        return sessionVo == null ? null : sessionVo.getCreateTime();
    }

    public String getId() {
        return this.getSessionId();
    }

    public long getLastAccessedTime() {
        return System.currentTimeMillis();
    }

    public int getMaxInactiveInterval() {
        return this.maxInactiveInterval;
    }

    public ServletContext getServletContext() {
        return this.isHttpRequest ? this.session.getServletContext() : null;
    }

    public HttpSessionContext getSessionContext() {
        return this.isHttpRequest ? this.session.getSessionContext() : null;
    }

    public Object getValue(String arg0) {
        SessionVO sessionVo = this.getSessionVo();
        return sessionVo == null ? null : sessionVo.getAttribute(arg0);
    }

    public String[] getValueNames() {
        SessionVO sessionVo = this.getSessionVo();
        return sessionVo == null ? null : (String[])sessionVo.getAttributes().keySet().toArray(new String[0]);
    }

    public boolean isNew() {
        SessionVO sessionVo = this.getSessionVo();
        return sessionVo == null ? null : sessionVo.isNew();
    }

    public void putValue(String arg0, Object arg1) {
        SessionVO sessionVo = this.getSessionVo();
        if (sessionVo != null) {
            sessionVo.setAttribute(arg0, arg1);
            this.setSessionVo(sessionVo);
        }
    }

    public void removeAttribute(String arg0) {
        SessionVO sessionVo = this.getSessionVo();
        if (sessionVo != null) {
            sessionVo.removeAttribute(arg0);
            this.setSessionVo(sessionVo);
        }
    }

    public void removeValue(String arg0) {
        SessionVO sessionVo = this.getSessionVo();
        if (sessionVo != null) {
            sessionVo.removeAttribute(arg0);
            this.setSessionVo(sessionVo);
        }
    }

    public void setMaxInactiveInterval(int arg0) {
        SessionVO sessionVo = this.getSessionVo();
        if (sessionVo != null) {
            sessionVo.setMaxInactiveInterval(arg0);
            this.setSessionVo(sessionVo);
        }
    }

    public Object getAttribute(String arg0) {
        SessionVO sessionVo = this.getSessionVo();
        return sessionVo == null ? null : sessionVo.getAttribute(arg0);
    }

    public void setAttribute(String arg0, Object arg1) {
        SessionVO sessionVo = this.getSessionVo();
        if (sessionVo != null) {
            sessionVo.setAttribute(arg0, arg1);
            this.setSessionVo(sessionVo);
        }
    }

    public void invalidate() {
        if (this.isHttpRequest) {
            ;
        }

        String sessionId = this.getSessionId();
        if (sessionId != null) {

            //之前的设计是，是由一个会话中心的应用来管理session
            //其实，我们可以改成把session放到redis或者其他地方

//            ISessionService sessionService = (ISessionService)BeanFactory.getBean(ISessionService.class);
//            sessionService.delSession(sessionId);

            //这里做的是删除session TODO


            if (this.isHttpRequest) {
                Cookie[] cookies = this.request.getCookies();
                if (cookies != null && cookies.length > 0) {
                    Cookie[] arr$ = cookies;
                    int len$ = cookies.length;

                    for(int i$ = 0; i$ < len$; ++i$) {
                        Cookie c = arr$[i$];
                        if (c.getName().toUpperCase().equals(this.sessionCookieName.toUpperCase())) {
                            c.setMaxAge(0);
                            this.response.addCookie(c);
                            return;
                        }
                    }
                }
            }

        }
    }

    private SessionVO getSessionVo() {
        String sessionId = this.getSessionId();
        if (sessionId == null) {
            return null;
        } else {

            //根据sessionId获取session,后面改成从redis中取session TODO

//            ISessionService sessionService = (ISessionService)BeanFactory.getBean(ISessionService.class);
//            String sessionStr = sessionService.getSession(sessionId);

            //这个session,可以暂时模拟
            String sessionStr = (String) MemoryCache.getSessionCache(sessionId);

            SessionVO sessionVo = (SessionVO) JSONObject.parseObject(sessionStr, SessionVO.class);
            return sessionVo;
        }
    }

    private void setSessionVo(SessionVO sessionVo) {

        //设置session,TODO
        //设计session存储位置，按照设计的思路存即可

//        ISessionService sessionService = (ISessionService)BeanFactory.getBean(ISessionService.class);
//        sessionService.setSession(JsonUtil.toJSONString(sessionVo));

        //模拟
        MemoryCache.setSessionCache(this.getSessionId(), JSONObject.toJSON(sessionVo).toString());


    }

    public boolean hasSession() {
        return this.getSessionVo() != null;
    }

    public void createSession() {
        SessionVO sessionVo = new SessionVO();
        sessionVo.setMaxInactiveInterval(this.maxInactiveInterval);
        sessionVo.setCreateTime((new Date()).getTime());
        if (this.sessionId != null) {
            sessionVo.setId(this.sessionId);
        }

        if (this.isHttpRequest) {
            this.sessionId = sessionVo.getId();
            Cookie c = new Cookie(this.sessionCookieName, this.sessionId);
            c.setPath("/");
            if (this.cookieDomain != null && !"".equals(this.cookieDomain)) {
                c.setDomain(this.cookieDomain);
            }

            this.response.addCookie(c);
        }

        this.setSessionVo(sessionVo);
    }

    private String getSessionId() {
        if (this.sessionId != null) {
            return this.sessionId;
        } else if (this.isHttpRequest) {
            String sessionId = this.request.getParameter(this.sessionCookieName);
            if (sessionId != null) {
                this.sessionId = sessionId;
                Cookie c = new Cookie(this.sessionCookieName, this.sessionId);
                c.setPath("/");
                if (this.cookieDomain != null && !"".equals(this.cookieDomain)) {
                    c.setDomain(this.cookieDomain);
                }

                this.response.addCookie(c);
                return sessionId;
            } else {
                Cookie[] cookies = this.request.getCookies();
                if (cookies != null && cookies.length > 0) {
                    Cookie[] arr$ = cookies;
                    int len$ = cookies.length;

                    for(int i$ = 0; i$ < len$; ++i$) {
                        Cookie c = arr$[i$];
                        if (c.getName().toUpperCase().equals(this.sessionCookieName.toUpperCase())) {
                            return c.getValue();
                        }
                    }
                }

                if ((this.sessionId == null || this.sessionId.trim().length() == 0) && this.session != null) {
                    this.sessionId = this.session.getId();
                    return this.sessionId;
                } else {
                    return null;
                }
            }
        } else {
            return null;
        }
    }
}

