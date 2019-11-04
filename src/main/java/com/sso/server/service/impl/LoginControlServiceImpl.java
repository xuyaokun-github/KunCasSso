package com.sso.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sso.server.cache.CustomCacheManager;
import com.sso.server.exception.KunLoginException;
import com.sso.server.service.ILoginControlService;
import com.sso.server.utils.RedissonUtil;
import com.sso.server.vo.SessionVO;
import org.jasig.cas.CentralAuthenticationService;
import org.redisson.api.RDeque;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 登录控制服务层
 *
 * Created by xuyaokun On 2019/10/31 23:24
 * @desc:
 */
@Service
public class LoginControlServiceImpl implements ILoginControlService{


    @Value("${login.control.enable}")
    private boolean enable;

    @Value("${login.control.maxUserNum}")
    private int maxUserNum;//最大同时在线用户数

    /**
     * 踢出之前登录的/之后登录的用户 默认踢出之前登录的用户
     */
    @Value("${login.control.kickoutAfter}")
    private boolean kickoutAfter;

    private static final String LOCKKEY_PREFIX = "login-control-lock_";
    private static final String USERKEY_PREFIX = "login-control-deque_";

    /**
     * 统一权限服务层
     */
    @Autowired
    @Qualifier("centralAuthenticationService")
    private CentralAuthenticationService centralAuthenticationService;

    @Override
    public void loginControl(String username, String ssoSessionId) throws KunLoginException {

        if (!enable) return;

        //以下操作上锁
        RLock lock = RedissonUtil.getRLock(LOCKKEY_PREFIX + username);
        lock.lock();

        try{
            RDeque<String> deque = RedissonUtil.getRDeque(USERKEY_PREFIX + username);
            if (!deque.contains(ssoSessionId)) {
                //入栈
                deque.push(ssoSessionId);
            }

            while (deque.size() > maxUserNum) {

                //待踢出的sessionId
                String kickoutSessionId;
                //根据策略，决定踢出前者还是后者
                if (kickoutAfter) {
                    //如果踢出后者，要考虑清除旧的kickoutSessionId，因为旧的kickoutSessionId可能早就已经退出了
                    //旧的kickoutSessionId会影响后续新登录的用户
                    //为了严谨，要判断用户会话是否还有效
                    String oldSessionId = deque.getLast();//旧的session
                    if(CustomCacheManager.getSessionCache(oldSessionId) != null){
                        kickoutSessionId = deque.removeFirst();
                        CustomCacheManager.deleteSession(kickoutSessionId);
                        //直接抛出异常，让用户登录失败即可
                        throw new KunLoginException("登录失败，相同账号已在别处登录！");
                    }else {
                        //假如已过期，删最初的即可
                        deque.removeLast();
                    }

                } else {
                    //如果踢出前者
                    kickoutSessionId = deque.removeLast();
                    //踢出前者，必须要清掉前者所用浏览器的cookie-TGC，否则还会继续免登录
                    String sessionStr = CustomCacheManager.getSessionCache(kickoutSessionId);
                    SessionVO sessionVo = JSONObject.parseObject(sessionStr, SessionVO.class);
                    //如何获取到TGT
                    //这里的设计是将TGT放到会话中，通过自定义的会话VO直接取出
                    String ticketGrantingTicket = sessionVo.getTgtId();
                    this.centralAuthenticationService.destroyTicketGrantingTicket(ticketGrantingTicket);

                    //开始踢人
                    System.out.println("踢出会话：" + kickoutSessionId);
                }
            }

        } finally {
            lock.unlock();
        }

    }
}
