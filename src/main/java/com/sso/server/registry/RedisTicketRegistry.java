
/**
 *
 */
package com.sso.server.registry;


import com.kunghsu.cache.CustomCacheManager;
import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.ticket.ServiceTicketImpl;
import org.jasig.cas.ticket.Ticket;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.jasig.cas.ticket.registry.encrypt.AbstractCrypticTicketRegistry;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;

import java.util.Collection;
import java.util.Map;

/**
 * @project web-sso-cas
 * @description Redis ticket存储器
 * @version 1.0
 * @history 修订历史（历次修订内容、修订人、修订时间等）
 */
public final class RedisTicketRegistry extends AbstractCrypticTicketRegistry implements DisposableBean {


	/**
	 * TGT cache entry timeout in seconds.
	 */
	@Value("#{${tgt.timeToKillInSeconds:7200}}")
	private int timeout;

	@Override
	public void addTicket(Ticket ticketToAdd) {
		expireSession(ticketToAdd);
		final Ticket ticket = encodeTicket(ticketToAdd);

        String appCLient = "false";
        if (ticket instanceof TicketGrantingTicket) {
            TicketGrantingTicket tgt = (TicketGrantingTicket) ticketToAdd;
            appCLient = (String) tgt.getAuthentication().getPrincipal().getAttributes().get("APP_CLIENT");
        } else {
            ServiceTicketImpl st = (ServiceTicketImpl) ticketToAdd;
            TicketGrantingTicket tgt = st.getGrantingTicket();
            appCLient = (String) tgt.getAuthentication().getPrincipal().getAttributes().get("APP_CLIENT");
        }

        //假如从登录成功后设置的值中判断 符合我们的预期，就做ticket的设置。
        if ("true".equals(appCLient)) {

        	//票据放到redis中保存

			//TODO 后续缓存肯定要改成用redis
//            ticketClient.set(ticket.getId(), ticket, 60 * 60 * 24 * 7 + "");

			//暂时写个简单的内存缓存做模拟
			CustomCacheManager.setCache(ticket.getId(), ticket, 60 * 60 * 24 * 7 + "");
            return;
        }
        //
//		ticketClient.set(ticket.getId(), ticket, timeout + "");
		CustomCacheManager.setCache(ticket.getId(), ticket, timeout + "");

	}

	@Override
	public Ticket getTicket(String ticketIdToGet) {
		final String ticketId = encodeTicketId(ticketIdToGet);
		try {
			logger.debug("#######cookie不为空=###ticketId=="+ticketId);

			//TODO 根据ticketID从缓存中获取票据
//			RedisObjectClient ticketClient = RedisManager.getInstance().createClient("ticketCache");
//			final Ticket t = ticketClient.get(ticketId);

			//暂时用内存缓存来做模拟
//			Ticket t = CustomCacheManager.getCache(ticketId);

			Ticket t = (Ticket) CustomCacheManager.getCache(ticketId);

			if (t != null) {
				logger.debug("#######Ticket不为空=###ticketId=="+t);
				final Ticket result = decodeTicket(t);
				expireSession(result);
				return getProxiedTicketInstance(result);
			}

		} catch (final Exception e) {
			logger.error("Failed fetching {} ", ticketId, e);
		}
		return null;
	}

	@Override
	public boolean deleteTicket(String ticketIdToDel) {
		final String ticketId = encodeTicketId(ticketIdToDel);
		if (ticketId == null) {
			return false;
		}

		final Ticket ticket = getTicket(ticketId);
		if (ticket == null) {
			return false;
		}

		if (ticket instanceof TicketGrantingTicket) {
			logger.debug("Removing ticket children [{}] from the registry.", ticket);
			deleteChildren((TicketGrantingTicket) ticket);

			//删除TGT对应的session信息

			//TODO 从redis中删除session信息
//			RedisObjectClient ticketClient = RedisManager.getInstance().createClient("ticketCache");
//			ticketClient.delete(((TicketGrantingTicket) ticket).getAuthentication().getPrincipal().getId());

			//暂时先用内存缓存来做模拟
			CustomCacheManager.delete(((TicketGrantingTicket) ticket).getAuthentication().getPrincipal().getId());

		}

		logger.debug("Deleting ticket {}", ticketId);
		try {
			//TODO 从redis中删除票据
// 			RedisObjectClient ticketClient = RedisManager.getInstance().createClient("ticketCache");
//			ticketClient.delete(ticketId);

			//暂时先用内存缓存来做模拟
			CustomCacheManager.delete(ticketId);

			return true;
		} catch (final Exception e) {
			logger.error("Ticket not found or is already removed. Failed deleting {}", ticketId, e);
		}
		return false;
	}

	/**
	 * Delete the TGT service tickets.
	 *
	 * @param ticket the ticket
	 */
	private void deleteChildren(final TicketGrantingTicket ticket) {
		// delete service tickets
		final Map<String, Service> services = ticket.getServices();
		if (services != null && !services.isEmpty()) {
			for (final Map.Entry<String, Service> entry : services.entrySet()) {
				try {
					//TODO 改成从redis中移除
//					RedisObjectClient ticketClient = RedisManager.getInstance().createClient("ticketCache");
//					ticketClient.delete(entry.getKey());

					//暂时先用内存缓存来做模拟
					CustomCacheManager.delete(entry.getKey());

					logger.trace("Scheduled deletion of service ticket [{}]", entry.getKey());
				} catch (final Exception e) {
					logger.error("Failed deleting {}", entry.getKey(), e);
				}
			}
		}
	}

	@Override
	public Collection<Ticket> getTickets() {
		throw new UnsupportedOperationException("getTickets not supported.");
	}

	@Override
	public void destroy() throws Exception {
	}

	@Override
	protected void updateTicket(Ticket ticketToUpdate) {
		expireSession(ticketToUpdate);
		final Ticket ticket = encodeTicket(ticketToUpdate);
		logger.debug("Updating ticket {}", ticket);

//		RedisObjectClient ticketClient = RedisManager.getInstance().createClient("ticketCache");
        String appCLient = "false";
        if (ticket instanceof TicketGrantingTicket) {
            TicketGrantingTicket tgt = (TicketGrantingTicket) ticketToUpdate;
            appCLient = (String) tgt.getAuthentication().getPrincipal().getAttributes().get("APP_CLIENT");
        } else {
            ServiceTicketImpl st = (ServiceTicketImpl) ticketToUpdate;
            TicketGrantingTicket tgt = st.getGrantingTicket();
            appCLient = (String) tgt.getAuthentication().getPrincipal().getAttributes().get("APP_CLIENT");
        }
        if ("true".equals(appCLient)) {

        	//TODO
//            ticketClient.set(ticket.getId(), ticket, 60 * 60 * 24 * 7 + "");
			CustomCacheManager.setCache(ticket.getId(), ticket, 60 * 60 * 24 * 7 + "");
			return;
        }
		//TODO
//		ticketClient.set(ticket.getId(), ticket, timeout + "");
		CustomCacheManager.setCache(ticket.getId(), ticket, timeout + "");

	}

	@Override
	protected boolean needsCallback() {
		return true;
	}

	/**
	 * 如果当前操作的是TGT则刷新对应的ssoSession的超时时间，保证只要TGT还存在，session就一定还在
	 * @param ticket
	 */
	private void expireSession(Ticket ticket){
		if (ticket instanceof TicketGrantingTicket) {
			String sessionId = ((TicketGrantingTicket)ticket).getAuthentication().getPrincipal().getId();
			logger.debug("expireSession [{}] in [{}] seconds.", sessionId,this.timeout);
			//刷新TGT对应的session信息的超时时间

			//之前的设计是通过一个会话中心来存储session
//			ISessionService sessionService = BondeInterfaceFactory.getInstance().getInterface(ISessionService.class);

			TicketGrantingTicket tgt = (TicketGrantingTicket) ticket;
            String appCLient = (String) tgt.getAuthentication().getPrincipal().getAttributes().get("APP_CLIENT");

            if ("true".equals(appCLient)) {

            	//假如后面改成用redis来存session之后，要更新redis里的过期时间 TODO

            	//设置session的过期时间
//                sessionService.expireSession(tgt.getAuthentication().getPrincipal().getId(), 60 * 60 * 24 * 7 + "");


                return;
            }


			//假如后面改成用redis来存session之后，要更新redis里的过期时间 TODO
//            sessionService.expireSession(tgt.getAuthentication().getPrincipal().getId(), this.timeout + "");

		}
	}
}
