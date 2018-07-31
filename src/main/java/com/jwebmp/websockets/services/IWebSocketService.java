package com.jwebmp.websockets.services;

import com.jwebmp.websockets.JWebMPSocket;
import com.jwebmp.websockets.options.WebSocketMessageReceiver;

import javax.websocket.Session;
import java.util.Comparator;

public interface IWebSocketService
		extends Comparable<IWebSocketService>, Comparator<IWebSocketService>
{

	default int compare(IWebSocketService o1, IWebSocketService o2)
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

	default int compareTo(IWebSocketService o)
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

	void onOpen(Session session, JWebMPSocket socket);

	void onClose(Session session, JWebMPSocket socket);

	void onMessage(String message, Session session, WebSocketMessageReceiver messageReceiver, JWebMPSocket socket);

	void onError(Throwable t, JWebMPSocket socket);
}
