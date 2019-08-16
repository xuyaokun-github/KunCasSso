/**
 *
 */
package com.sso.server.principal;

import com.alibaba.fastjson.JSONObject;
import com.kunghsu.cache.CustomCacheManager;
import com.kunghsu.vo.SessionVO;
import com.sso.server.utils.SsoUtil;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.services.RegisteredServiceUsernameAttributeProvider;

/**
 *
 * @project web-sso-cas
 * @description 根据Principal的ID拼装返回给客户端的数据
 * @version 1.0
 * @history 修订历史（历次修订内容、修订人、修订时间等）
 *
 */
public class KunRegisteredServiceUsernameProvider  implements RegisteredServiceUsernameAttributeProvider {

	/**
	 *
	 */
	private static final long serialVersionUID = -8637625065778127635L;

	@Override
	public String resolveUsername(Principal principal, Service service) {

		// 从会话中心中取出sessionId对应的会话信息
//		ISessionService sessionService = BondeInterfaceFactory.getInstance().getInterface(ISessionService.class);
//		String sessionStr = sessionService.getSession(principal.getId());
//		SessionVO sessionVo = JsonUtil.getObjectForJsonString(sessionStr, SessionVO.class);
//		return JsonUtil.getJsonStringForJavaPOJO(sessionVo.getAttribute(SsoConstant.SSO_SESSION_KEY));


		//原本在services/HTTPSandHTTP-10000001-Tim.json中使用的是org.jasig.cas.services.DefaultRegisteredServiceUsernameProvider

		//TODO 这个方法主要就是用于从会话中心获取到session，然后把session里的单点登录vo取出来

		String principalId = principal.getId();
		//根据principalId取到session信息，然后根据session取到会话中的单点登录VO
//		String str = JSONObject.fromObject(new SsoLoginInfoVo()).toString();
//		return str;

		String sessionStr = (String) CustomCacheManager.getSessionCache(principal.getId());
		SessionVO sessionVo = JSONObject.parseObject(sessionStr, SessionVO.class);
		return JSONObject.toJSON(sessionVo.getAttribute(SsoUtil.SSO_SESSION_KEY)).toString();
	}


}
