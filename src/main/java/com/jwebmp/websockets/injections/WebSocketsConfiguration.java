package com.jwebmp.websockets.injections;

import com.jwebmp.guicedinjection.GuiceContext;
import com.jwebmp.guicedinjection.interfaces.IGuicePreStartup;
import com.jwebmp.websockets.services.IWebSocketPreConfiguration;

import java.util.ServiceLoader;
import java.util.Set;
import java.util.TreeSet;

public class WebSocketsConfiguration
		implements IGuicePreStartup<WebSocketsConfiguration>
{
	@Override
	public void onStartup()
	{
		Set<IWebSocketPreConfiguration> loader = GuiceContext.instance()
		                                                     .getLoader(IWebSocketPreConfiguration.class, true, ServiceLoader.load(IWebSocketPreConfiguration.class));
		Set<IWebSocketPreConfiguration> sortedSet = new TreeSet<>();
		loader.forEach(sortedSet::add);
		sortedSet.forEach(IWebSocketPreConfiguration::configure);
	}
}
