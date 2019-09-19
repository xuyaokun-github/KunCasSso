/**
 *
 */
package com.sso.server.authencation;

import com.kunghsu.verify.ICredentialVerifyService;
import com.sso.server.credential.BaseCredential;
import com.sso.server.filter.SessionFilter;
import com.sso.server.utils.SsoUtil;
import com.sso.server.vo.SsoLoginInfoVo;
import org.jasig.cas.authentication.Credential;
import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.PreventedException;

import javax.annotation.Resource;
import java.security.GeneralSecurityException;

import static com.sso.server.constant.SsoContant.SSO_SESSION_KEY;

/**
 *
 * @project web-sso-cas
 * @description 用户名密码校验器
 * @version 1.0
 * @history 修订历史（历次修订内容、修订人、修订时间等）
 *
 */
public class UsernamePasswordAuthencationHandler extends BaseAbstractAuthencationHandler {


	@Resource
	private ICredentialVerifyService credentialVerifyService;

	boolean checkCaptchCode = true;//是否校验验证码

	public void setCheckCaptchCode(boolean checkCaptchCode) {
		this.checkCaptchCode = checkCaptchCode;
	}

	@Override
	protected HandlerResult doAuthentication(Credential credential) throws GeneralSecurityException, PreventedException {

		BaseCredential baseCredential = (BaseCredential)credential;
		//是否需要验证验证码
		if(this.checkCaptchCode){
//			this.checkCaptchCode(baseCredential);
			checkSliderVerificationCode(baseCredential);
		}

		//拿到sessionID
		String ssoSessionId = SessionFilter.getRequest().getSession().getId();

		//开始做登录验证操作
		SsoLoginInfoVo ssoLoginInfoVo = credentialVerifyService.verify(baseCredential);

		//注意，登录成功，要创建session
		SsoUtil.createSession(ssoSessionId, ssoLoginInfoVo, "1");
		SessionFilter.getRequest().getSession().setAttribute(SSO_SESSION_KEY, ssoLoginInfoVo);

		//返回结果
		return createHandlerResult(credential, this.principalFactory.createPrincipal(ssoSessionId), null);
	}


}