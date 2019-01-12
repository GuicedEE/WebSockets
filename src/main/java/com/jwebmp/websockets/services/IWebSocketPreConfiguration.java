package com.jwebmp.websockets.services;

import com.jwebmp.guicedinjection.interfaces.IDefaultService;
import com.jwebmp.guicedinjection.interfaces.IServiceEnablement;

/**
 * A service for JWebMPWebSockets to configure app servers
 */
public interface IWebSocketPreConfiguration<J extends IWebSocketPreConfiguration<J>>
		extends IDefaultService<J>, IServiceEnablement<J>
{
	void configure();
}
