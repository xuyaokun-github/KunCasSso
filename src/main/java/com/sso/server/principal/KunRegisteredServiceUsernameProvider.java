/**
 *
 */
package com.sso.server.principal;

import com.alibaba.fastjson.JSONObject;
import com.sso.server.cache.CustomCacheManager;
import com.sso.server.vo.SessionVO;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.services.RegisteredServiceUsernameAttributeProvider;

import static com.sso.server.constant.SsoContant.SSO_SESSION_KEY;

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

		//原本在services/HTTPSandHTTP-10000001-Tim.json中使用的是org.jasig.cas.services.DefaultRegisteredServiceUsernameProvider

		/*
			用KunRegisteredServiceUsernameProvider替代了DefaultRegisteredServiceUsernameProvider
			DefaultRegisteredServiceUsernameProvider原本只是简单地返回了principalId
			复写它，定制化目的在于：用于从会话中心获取到session，然后把session里的单点登录vo取出来
			最终返回单点登录vo对应的序列化字符串
		 */

		//principalId实质就是sessionId，这个sessionId会服务于多个客户端
		String principalId = principal.getId();
		//根据principalId取到session对象，然后根据session取到会话中的单点登录VO
		String sessionStr = (String) CustomCacheManager.getSessionCache(principal.getId());
		SessionVO sessionVo = JSONObject.parseObject(sessionStr, SessionVO.class);
		return JSONObject.toJSON(sessionVo.getAttribute(SSO_SESSION_KEY)).toString();
	}


}
