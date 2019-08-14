package com.kunghsu.filter;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class HttpServletRequestWrap extends HttpServletRequestWrapper {
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final String sessionCookieName;
    private SessionWrap sessionProxy;
    private final String cookieDomain;

    public HttpServletRequestWrap(HttpServletRequest request, HttpServletResponse response, String sessionCookieName, String cookieDomain) {
        super(request);
        this.request = request;
        this.response = response;
        this.sessionCookieName = sessionCookieName;
        this.cookieDomain = cookieDomain;
    }

    public HttpSession getSession(boolean create) {
        HttpSession session = super.getSession(true);
        this.sessionProxy = this.sessionProxy == null ? new SessionWrap(session, this.request, this.response, this.sessionCookieName, this.cookieDomain) : this.sessionProxy;
        boolean hasSession = this.sessionProxy.hasSession();
        if (create && !hasSession) {
            this.sessionProxy.createSession();
        } else if (!create && !hasSession) {
            this.sessionProxy = null;
        }

        return this.sessionProxy;
    }

    public HttpSession getSession() {
        return this.getSession(true);
    }
}
