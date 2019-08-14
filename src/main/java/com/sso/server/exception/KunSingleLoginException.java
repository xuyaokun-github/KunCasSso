package com.sso.server.exception;


import com.kunghsu.vo.SsoLoginResultVo;

import java.security.GeneralSecurityException;

public class KunSingleLoginException extends GeneralSecurityException {
	public static ThreadLocal<SsoLoginResultVo> errMsg = new ThreadLocal<SsoLoginResultVo>();
	/**
	 *
	 */
	private static final long serialVersionUID = -6054906358515523211L;

	public KunSingleLoginException() {
		super();
	}

	public KunSingleLoginException(SsoLoginResultVo resultVo) {
		super(resultVo.getErrCode());
		errMsg.set(resultVo);
	}
}
