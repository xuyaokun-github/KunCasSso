package com.sso.server.service;

import com.sso.server.exception.KunLoginException;

public interface ILoginControlService {

    void loginControl(String username, String ssoSessionId) throws KunLoginException;

}
