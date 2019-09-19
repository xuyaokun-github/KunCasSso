package com.kunghsu.verify;

import com.sso.server.credential.BaseCredential;
import com.sso.server.exception.KunLoginException;
import com.sso.server.vo.SsoLoginInfoVo;

public interface ICredentialVerifyService {

    SsoLoginInfoVo verify(BaseCredential baseCredential) throws KunLoginException;

}
