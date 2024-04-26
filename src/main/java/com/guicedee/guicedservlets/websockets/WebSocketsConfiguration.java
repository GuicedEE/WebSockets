package com.guicedee.guicedservlets.websockets;

import com.guicedee.client.*;
import com.guicedee.guicedinjection.interfaces.*;
import com.guicedee.guicedservlets.websockets.services.*;

import java.util.*;

public class WebSocketsConfiguration
				implements IGuicePreStartup<WebSocketsConfiguration>
{
	@Override
	public void onStartup()
	{
		Set<IWebSocketPreConfiguration> loader = IGuiceContext.loaderToSetNoInjection(ServiceLoader.load(IWebSocketPreConfiguration.class));
		Set<IWebSocketPreConfiguration> sortedSet = new TreeSet<>(loader);
		sortedSet.removeIf(preConfiguration -> !preConfiguration.enabled());
		sortedSet.forEach(IWebSocketPreConfiguration::configure);
	}
}
