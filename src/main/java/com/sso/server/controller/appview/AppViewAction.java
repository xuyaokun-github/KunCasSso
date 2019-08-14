package com.sso.server.controller.appview;

import org.springframework.binding.message.Message;
import org.springframework.binding.message.MessageContext;
import org.springframework.stereotype.Component;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.core.collection.ParameterMap;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

@Component("appViewAction")
public class AppViewAction extends AbstractAction {

	@Override
	protected Event doExecute(RequestContext context) throws Exception {
		// TODO Auto-generated method stub
		ParameterMap map = context.getRequestParameters();

		MessageContext messageContext = context.getMessageContext();
		Message[] allMessages = messageContext.getAllMessages();
		Message[] messages = messageContext.getAllMessages();
		MutableAttributeMap<Object> requestScope = context.getRequestScope();
		for (Message message : messages) {
			requestScope.put("errCode", message.getSource());
			requestScope.put("errMsg", message.getText());
		}
		System.out.println("////////////////" + map);
		MutableAttributeMap<Object> aMap = context.getRequestScope();
		MutableAttributeMap<Object> flowScope = context.getFlowScope();

		System.out.println(aMap);
		System.out.println(flowScope);

		return success();
	}

}
