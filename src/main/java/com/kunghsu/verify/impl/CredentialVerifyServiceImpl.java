package com.kunghsu.verify.impl;

import com.kunghsu.verify.ICredentialVerifyService;
import com.sso.server.credential.BaseCredential;
import com.sso.server.exception.KunLoginException;
import com.sso.server.vo.SsoLoginInfoVo;
import org.springframework.stereotype.Component;

/**
 * 统一验证器
 *
 * Created by xuyaokun On 2019/9/19 23:17
 * @desc:
 */
@Component
public class CredentialVerifyServiceImpl implements ICredentialVerifyService{

    @Override
    public SsoLoginInfoVo verify(BaseCredential baseCredential) throws KunLoginException {

        //至于校验是否登录成功，可以查数据库或者调用用户中心来判断用户是否能登录


        //假如登录不成功，可以抛出异常

        //TODO
        System.out.println("username:" + baseCredential.getUserName());
        System.out.println("passWord:" + baseCredential.getPassWord());

        if (!"xyk".equals(baseCredential.getUserName())){
            throw new KunLoginException("用户不合法，请重新登录");
        }

        // 假如登录成功，继续往下执行
        //封装单点登录信息，这类信息可能各个客户端都会从中取出关键业务信息
        //可以使用自定义的对象,客户端可以把这个VO从断言对象中取出来
        SsoLoginInfoVo ssoLoginInfoVo = new SsoLoginInfoVo();
        ssoLoginInfoVo.setUsername(baseCredential.getUserName());
        ssoLoginInfoVo.setPassword(baseCredential.getPassWord());
        ssoLoginInfoVo.add("time", "" + System.currentTimeMillis());
        return ssoLoginInfoVo;

    }
}
