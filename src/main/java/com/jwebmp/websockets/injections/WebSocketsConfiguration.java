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
	/**
	 * Enables Geo Bytes data to come through the web socket
	 */
	private static boolean geoBytesEnabled;
	/**
	 * Enables the local storage values to come in
	 */
	private static boolean localStorageEnabled;

	/**
	 * Getter for property 'geoBytesEnabled'.
	 *
	 * @return Value for property 'geoBytesEnabled'.
	 */
	public static boolean isGeoBytesEnabled()
	{
		return geoBytesEnabled;
	}

	/**
	 * Setter for property 'geoBytesEnabled'.
	 *
	 * @param geoBytesEnabled
	 * 		Value to set for property 'geoBytesEnabled'.
	 */
	public static void setGeoBytesEnabled(boolean geoBytesEnabled)
	{
		WebSocketsConfiguration.geoBytesEnabled = geoBytesEnabled;
	}

	/**
	 * Getter for property 'localStorageEnabled'.
	 *
	 * @return Value for property 'localStorageEnabled'.
	 */
	public static boolean isLocalStorageEnabled()
	{
		return localStorageEnabled;
	}

	/**
	 * Setter for property 'localStorageEnabled'.
	 *
	 * @param localStorageEnabled
	 * 		Value to set for property 'localStorageEnabled'.
	 */
	public static void setLocalStorageEnabled(boolean localStorageEnabled)
	{
		WebSocketsConfiguration.localStorageEnabled = localStorageEnabled;
	}

	@Override
	public void onStartup()
	{
		Set<IWebSocketPreConfiguration> loader = GuiceContext.instance()
		                                                     .getLoader(IWebSocketPreConfiguration.class, true, ServiceLoader.load(IWebSocketPreConfiguration.class));
		Set<IWebSocketPreConfiguration> sortedSet = new TreeSet<>(loader);
		sortedSet.removeIf(preConfiguration -> !preConfiguration.enabled());
		sortedSet.forEach(IWebSocketPreConfiguration::configure);
	}
}
