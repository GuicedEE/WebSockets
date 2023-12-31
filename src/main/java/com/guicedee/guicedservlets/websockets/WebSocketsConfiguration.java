package com.guicedee.guicedservlets.websockets;

import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedinjection.interfaces.IDefaultService;
import com.guicedee.guicedinjection.interfaces.IGuicePreStartup;
import com.guicedee.guicedservlets.websockets.services.IWebSocketPreConfiguration;

import java.util.ServiceLoader;
import java.util.Set;
import java.util.TreeSet;

public class WebSocketsConfiguration
				implements IGuicePreStartup<WebSocketsConfiguration>
{
	@Override
	public void onStartup()
	{
		Set<IWebSocketPreConfiguration> loader = GuiceContext.instance().loaderToSetNoInjection(ServiceLoader.load(IWebSocketPreConfiguration.class));
		Set<IWebSocketPreConfiguration> sortedSet = new TreeSet<>(loader);
		sortedSet.removeIf(preConfiguration -> !preConfiguration.enabled());
		sortedSet.forEach(IWebSocketPreConfiguration::configure);
	}
}
