package com.sso.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.sso.server.cache.CustomCacheManager;
import com.sso.server.vo.SessionVO;
import com.sso.server.utils.SsoUtil;
import org.jasig.cas.web.support.WebUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;

import static com.sso.server.constant.SsoContant.SSO_SESSION_KEY;
import static com.sso.server.controller.BaseController.trim;


/**
 * 提供非web应用客户端服务调用(非web应用)
 *
 */
@Controller
@Scope("prototype")
public class AppController {

	/**
	 * 登录成功后，返回TGC
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/kun/singlePage", produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String singlePage(HttpServletRequest request) {

		String errMsg = trim(request.getAttribute("errMsg"));
		String errCode = trim(request.getAttribute("errCode"));
		String loginstate = trim(request.getAttribute("loginstate"));
		String single = trim(request.getAttribute("single"));
		JSONObject json = new JSONObject();

		String id = request.getSession().getId();
		//下发tgt
		RequestContext context = RequestContextHolder.getRequestContext();
		String tgtId = WebUtils.getTicketGrantingTicketId(context);

		if (!"".equals(errCode)) {
			System.out.println("非web应用客户端登录失败");
			json.put("kun_tgc", id);
			json.put("tgt",tgtId);
			json.put("success", false);
			json.put("message", errMsg);
			json.put("code", errCode);
		} else {
			System.out.println("非web应用客户端登录成功");
			//Cookie[] cookies = request.getCookies();
			//System.out.println(cookies);
			//System.out.println(id);
			json.put("kun_tgc", id);
			json.put("tgt",tgtId);
			json.put("loginstate", loginstate);
			json.put("success", true);
			json.put("message", "OK");
			json.put("ssoLoginInfo", SsoUtil.getSsoLoginInfo(request));
		}

		//可以根据single参数处理不同的客户端类型
		if("6".equals(single)) {

		}else if("3".equals(single)){

		}
		//TODO 报文交互的加密操作
		return json.toJSONString();
	}

	/**
	 * 根据客户端的TGC,获取单点登录信息VO
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/kun/getSsoLoginInfo", produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getSsoLoginInfo(HttpServletRequest request) {

		String kunTgc = request.getParameter("kunTgc");//TGC就是sessionId
		String sessionStr = (String) CustomCacheManager.getSessionCache(kunTgc);
		SessionVO sessionVo = JSONObject.parseObject(sessionStr, SessionVO.class);
		return JSONObject.toJSONString(sessionVo.getAttribute(SSO_SESSION_KEY));

	}

}
