package com.jwebmp.websockets.services;

import java.util.Comparator;

/**
 * A service for JWebMPWebSockets to configure app servers
 */
public interface IWebSocketPreConfiguration
		extends Comparable<IWebSocketPreConfiguration>, Comparator<IWebSocketPreConfiguration>
{
	void configure();

	default int compare(IWebSocketPreConfiguration o1, IWebSocketPreConfiguration o2)
	{
		return o1.getSortOrder()
		         .compareTo(o2.getSortOrder());
	}

	/**
	 * Returns any applicable sort order or 0 is assumed
	 *
	 * @return The sort order
	 */
	default Integer getSortOrder()
	{
		return 100;
	}

	default int compareTo(IWebSocketPreConfiguration o)
	{
		if (o == null)
		{
			return -1;
		}
		if (getClass().equals(o.getClass()))
		{
			return 0;
		}
		int result = getSortOrder().compareTo(o.getSortOrder());
		return result == 0 ? 1 : result;
	}

}
