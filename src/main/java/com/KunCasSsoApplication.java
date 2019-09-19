package com;

import com.sso.server.listener.SystemStartUpListener;
import com.sso.server.filter.SessionFilter;
import org.jasig.cas.CasEnvironmentContextListener;
import org.jasig.inspektr.common.web.ClientInfoThreadLocalFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.HashMap;
import java.util.Map;

/**
 * 应用启动类
 *
 * Created by xuyaokun On 2019/8/13 22:27
 * @desc: 
 */
@ComponentScan(basePackages={"org.jasig.cas","com"})
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
@ImportResource(value = {
		"classpath:spring/deployerConfigContext.xml",
		"classpath:spring/spring-configuration/*.xml",
		"classpath:spring/spring-sso.xml"})
@ServletComponentScan
public class KunCasSsoApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {

		//注册监听器
		SpringApplication springApplication = new SpringApplication(KunCasSsoApplication.class);
		springApplication.addListeners(new SystemStartUpListener());
		springApplication.run(args);

	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		builder.listeners(new SystemStartUpListener());
		return builder.sources(KunCasSsoApplication.class);
	}


	//web.xml配置

	//定义servlet，注册
	@Bean
	public ServletRegistrationBean casServlet(){

		ServletRegistrationBean servletRegistrationBean =
				new ServletRegistrationBean(new DispatcherServlet(),"/login",
						"/logout",
						"/validate",
						"/serviceValidate",
						"/p3/serviceValidate",
						"/proxy",
						"/proxyValidate",
						"/p3/proxyValidate",
						"/CentralAuthenticationService",
						"/status",
						"/statistics",
						"/statistics/ssosessions/*",
						"/statistics/ssosessions",
						"/status/config/*",
						"/authorizationFailure.html",
						"/status/config",
						"/v1/*");
		Map<String, String> initParameters = new HashMap<>();
		initParameters.put("contextConfigLocation", "/WEB-INF/cas-servlet.xml,classpath*:/META-INF/cas-servlet-*.xml,/WEB-INF/cas-servlet-*.xml");
		initParameters.put("publishContext", "false");

		servletRegistrationBean.setInitParameters(initParameters);
		servletRegistrationBean.setLoadOnStartup(1);

		//这个setName必不可少，为了区分默认的DispatcherServlet
        servletRegistrationBean.setName("casContext");
		return servletRegistrationBean;
	}

	@Bean
	public FilterRegistrationBean clientInfoThreadLocalFilter() {
		FilterRegistrationBean frBean = new FilterRegistrationBean();
		frBean.setFilter(new ClientInfoThreadLocalFilter());
		frBean.addUrlPatterns("/*");
		return frBean;
	}

	@Bean
	public FilterRegistrationBean requestParameterSecurityFilter() {
		FilterRegistrationBean frBean = new FilterRegistrationBean();
		frBean.setFilter(new DelegatingFilterProxy());
		frBean.addUrlPatterns("/*");
		return frBean;
	}

	@Bean
	public FilterRegistrationBean responseHeadersSecurityFilter() {
		FilterRegistrationBean frBean = new FilterRegistrationBean();
		frBean.setFilter(new DelegatingFilterProxy());
		frBean.addUrlPatterns("/*");
		return frBean;
	}


	@Bean
	public FilterRegistrationBean sessionFilter() {
		FilterRegistrationBean frBean = new FilterRegistrationBean();
		frBean.setFilter(new SessionFilter());
		//.kun-gd.gov.cn
		frBean.addInitParameter("cookieDomain", "127.0.0.1");
		frBean.addUrlPatterns("/*");
		return frBean;
	}

	@Bean
    public ServletListenerRegistrationBean casEnvironmentContextListener() {
        ServletListenerRegistrationBean servletListenerRegistrationBean = new ServletListenerRegistrationBean();
		servletListenerRegistrationBean.setListener(new CasEnvironmentContextListener());
        return servletListenerRegistrationBean;
    }

}
