/**
 *
 */
package com.sso.server.authencation;

import com.sso.server.filter.SessionFilter;
import com.sso.server.credential.BaseCredential;
import com.sso.server.exception.KunLoginException;
import lombok.extern.slf4j.Slf4j;
import org.jasig.cas.authentication.Credential;
import org.jasig.cas.authentication.handler.support.AbstractPreAndPostProcessingAuthenticationHandler;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @project web-sso-cas
 * @description
 * @version 1.0
 * @history 修订历史（历次修订内容、修订人、修订时间等）
 *
 */
@Slf4j
public abstract class BaseAbstractAuthencationHandler extends AbstractPreAndPostProcessingAuthenticationHandler {

	boolean enable = true;//是否启用该校验器

	@Value("${login.sliderVerificationCode.ak}")
	private String ACCESS_KEY;
	@Value("${login.sliderVerificationCode.sk}")
	private String ACCESS_SECRET;

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	@Override
	public boolean supports(Credential credential) {
		if (this.enable && credential instanceof BaseCredential
				&& this.getClass().getSimpleName().equals(((BaseCredential) credential).getAuthencationHandler())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 校验普通验证码
	 * 如果验证码错误则直接抛出CaptchaException
	 * @throws KunLoginException
	 */
	protected void checkCaptchCode(BaseCredential credential) throws KunLoginException {

		//该方法的主要作用，校验验证码，假如有异常就抛出异常

	}


	/**
	 * 这个方法是之前设计留下来做滑块验证的
	 *
	 * @param credential
	 * @throws KunLoginException
	 */
	protected void checkSliderVerificationCode(BaseCredential credential) throws KunLoginException {

		HttpServletRequest request = SessionFilter.getRequest();
		String csessionid = credential.getCsessionid();//之前做滑块验证设计的字段
		String sig = credential.getSig();
		String token = credential.getToken();
		String scene = credential.getScene();

	}

	/**
	 * 验证不通过时
	 * 从REDIS移除的验证码（验证码之前的设计应该是放在redis中）
	 */
	protected void removeCaptchCodeFromRedis(BaseCredential credential) {

		String reqCaptchCode = credential.getCaptchCode();
		//根据这个reqCaptchCode，执行从redis删除字符串的逻辑，reqCaptchCode就是key
		//TODO
    }

}
