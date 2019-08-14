
/**
 *
 */
package com.sso.server.exception;

/**
 * @project web-sso-cas
 * @description
 * @version 1.0
 * @history 修订历史（历次修订内容、修订人、修订时间等）
 */
public class KunLoginException extends java.security.GeneralSecurityException{

	public static ThreadLocal<String> errMsg = new ThreadLocal<String>();
	/**
	 *
	 */
	private static final long serialVersionUID = -6054906358515523211L;

	public KunLoginException() {
		super();
	}

	public KunLoginException(String msg) {
		super(msg);
		errMsg.set(msg);
	}
}
