package org.jasig.cas.web.flow;

import org.jasig.cas.authentication.Credential;
import org.springframework.binding.message.MessageContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

public interface IAuthenticationViaFormAction {

    Event submit(RequestContext context, Credential credential,
                 MessageContext messageContext);
}
