package com.kunghsu.vo;


import com.kunghsu.filter.HttpServletRequestWrap;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by xuyaokun On 2018/11/4 20:55
 * @desc: 
 */
public class SessionFilter implements Filter {
    protected String sessionCookieName;
    protected String cookieDomain;
    private static ThreadLocal<HttpServletRequest> threadLocalRequest = new ThreadLocal();

    public SessionFilter() {
        System.out.println("进入SessionFilter过滤器方法");
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        threadLocalRequest.set(new HttpServletRequestWrap((HttpServletRequest)request, (HttpServletResponse)response, this.sessionCookieName, this.cookieDomain));
        chain.doFilter((ServletRequest)threadLocalRequest.get(), response);
        threadLocalRequest.remove();
    }

    public void destroy() {
    }

    public void init(FilterConfig arg0) throws ServletException {
        this.sessionCookieName = "KUN_TGC";
        this.cookieDomain = arg0.getInitParameter("cookieDomain");
    }

    public static HttpServletRequest getRequest() {
        return (HttpServletRequest)threadLocalRequest.get();
    }
}
