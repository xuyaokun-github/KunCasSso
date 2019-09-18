
package com.sso.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kunghsu.vo.SsoLoginInfoVo;
import com.sso.server.utils.SsoUtil;
import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.logout.DefaultLogoutRequest;
import org.jasig.cas.logout.LogoutManager;
import org.jasig.cas.logout.LogoutRequestStatus;
import org.jasig.cas.logout.SingleLogoutService;
import org.jasig.cas.services.RegisteredService;
import org.jasig.cas.services.ServicesManager;
import org.jasig.cas.ticket.Ticket;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.jasig.cas.ticket.registry.TicketRegistry;
import org.jasig.cas.web.flow.FrontChannelLogoutAction;
import org.jasig.cas.web.support.CookieRetrievingCookieGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

/**
 *
 * @project web-sso
 * @description
 * @version 1.0
 * @history 修订历史（历次修订内容、修订人、修订时间等）
 *
 */
@Controller
@RequestMapping(value = "/auth")
public class AuthController {

    @Value("${login.single1_cipher_password}")
    private String single1CipherPassword;

	/**
	 * 日志
	 */
	private final Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	/** CookieGenerator for TGT Cookie. */

	@Autowired
	@Qualifier("ticketGrantingTicketCookieGenerator")
	private CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator;


	/** {@link TicketRegistry}  for storing and retrieving tickets as needed. */
	@Resource(name="redisTicketRegistry")
//	@Resource(name="ticketRegistry")
	protected TicketRegistry ticketRegistry;

	/** The services manager. */
	@Autowired
	@Qualifier("servicesManager")
	private ServicesManager servicesManager;

	@Autowired
	@Qualifier("logoutManager")
	private LogoutManager logoutManager;


	String casServerUrlPrefix;


	/**
	 * 检查是否已经登录成功，如果是，则返回用户的关联企业供用户选择
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value = "/checkLoginState.do")
	public @ResponseBody
    Map<String,Object> checkLoginState(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		Map<String,Object> responseData = new HashMap<String,Object>();
		SsoLoginInfoVo loginInfo = SsoUtil.getSsoLoginInfo(request);

		if(loginInfo == null){//登录未成功
			responseData.put("flag", "error");
			responseData.put("errMsg", "数据处理异常");
		}else{
			Cookie cookie = WebUtils.getCookie(request, "TGC");
			Ticket ticket = null;
			logger.debug("#######cookie=###"+cookie);
			if (cookie != null) {
				logger.debug("#######cookie不为空=###"+cookie.getValue());
				ticket = this.ticketRegistry.getTicket(cookie.getValue());
			}
			if ((ticket == null) || (ticket.isExpired())){
				responseData.put("flag", "error");
				request.getSession().removeAttribute("loginInfo");
			}else{
				responseData.put("flag", "ok");
			}
		}
		return responseData;
	}

	/**
	 * 获取登录用户信息
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value = "/getSsoLoginInfo.do")
	public void getSsoLoginInfo(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		Map<String,Object> responseData = new HashMap<String,Object>();
		SsoLoginInfoVo loginInfo = SsoUtil.getSsoLoginInfo(request);
		if(loginInfo == null){//登录未成功
			responseData.put("flag", "error");
		}else{
			responseData.put("flag", "ok");
			responseData.put("ssoLoginInfo", loginInfo);
		}
		response.getWriter().write(new ObjectMapper().writeValueAsString(responseData));
	}


	/**
	 * 通知非共享会话的客户端应用身份已切换
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value = "/noticeChangeSf.do")
	public String noticeChangeSf(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		List<Map<String,String>> clients = new ArrayList<Map<String,String>>();

		String tgtId = this.ticketGrantingTicketCookieGenerator.retrieveCookieValue(request);
		final TicketGrantingTicket ticket = this.ticketRegistry.getTicket(tgtId, TicketGrantingTicket.class);
		final Map<String, Service> services = ticket.getServices();
		for (final Map.Entry<String, Service> entry : services.entrySet()) {
			final Service service = entry.getValue();
			if (service instanceof SingleLogoutService) {
				final RegisteredService registeredService = servicesManager.findServiceBy(service);
				final URL logoutUrl = determineLogoutUrl(registeredService, (SingleLogoutService)service);
				final DefaultLogoutRequest logoutRequest = new DefaultLogoutRequest(entry.getKey(), (SingleLogoutService)service, logoutUrl);
				logoutRequest.setStatus(LogoutRequestStatus.NOT_ATTEMPTED);

				String frontLogoutUrl = logoutRequest.getLogoutUrl().toExternalForm();
				final String logoutMessage = logoutManager.createFrontChannelLogoutMessage(logoutRequest);
				Map<String,String> client = new HashMap<String,String>();
				client.put("frontLogoutUrl", frontLogoutUrl);
				client.put("paramName", FrontChannelLogoutAction.DEFAULT_LOGOUT_PARAMETER);
				client.put("paramValue", logoutMessage);
				//只通知与sso不同域名的应用，因为凡是与sso应用同域名的应用都是共享会话的
				if(!checkIsSameDomain(frontLogoutUrl)){
					clients.add(client);
				}
			}
		}
		request.setAttribute("clientUrls", clients);
		return "noticeChangeSf";
	}

	/**
	 * Determine logout url.
	 *
	 * @param registeredService the registered service
	 * @param singleLogoutService the single logout service
	 * @return the uRL
	 */
	private URL determineLogoutUrl(final RegisteredService registeredService, final SingleLogoutService singleLogoutService) {
		try {
			URL logoutUrl = new URL(singleLogoutService.getOriginalUrl());
			final URL serviceLogoutUrl = registeredService.getLogoutUrl();

			if (serviceLogoutUrl != null) {
				logoutUrl = serviceLogoutUrl;
			}
			return logoutUrl;
		} catch (final Exception e) {
			throw new IllegalArgumentException(e);
		}
	}
	/**
	 * 检查sso的二级域名与客户端应用的二级域名是否一致
	 * @return
	 */
	private boolean checkIsSameDomain(String serverName){
		Properties properties = new Properties();
		InputStream input = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("properties/cas.properties");
		try {
			properties.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		casServerUrlPrefix = properties.getProperty("server.prefix");

		return this.getDomain(serverName).equals(this.getDomain(casServerUrlPrefix));
	}

	private String getDomain(String url){
		url = url.replace("http://", "");
		int endIndex = url.indexOf(":")==-1?url.indexOf("/"):url.indexOf(":");
		url = url.substring(0, endIndex);
		return url;
	}


}
