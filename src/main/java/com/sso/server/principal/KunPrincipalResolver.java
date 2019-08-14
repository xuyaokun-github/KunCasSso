/**
 *
 */
package com.sso.server.principal;

import org.jasig.cas.authentication.Credential;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.authentication.principal.PrincipalResolver;

/**
 *
 * @project web-sso-cas
 * @description Principal生成器，如果supports返回false，则直接使用AuthencationHandler返回的Principal
 * @version 1.0
 * @history 修订历史（历次修订内容、修订人、修订时间等）
 *
 */
public class KunPrincipalResolver implements PrincipalResolver {

	@Override
	public Principal resolve(Credential credential) {
		return null;
	}

	@Override
	public boolean supports(Credential credential) {
		return false;
	}

}
