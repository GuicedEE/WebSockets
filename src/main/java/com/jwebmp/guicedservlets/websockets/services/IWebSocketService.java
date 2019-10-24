package com.guicedee.guicedservlets.websockets.services;

import com.guicedee.guicedservlets.websockets.GuicedWebSocket;
import com.guicedee.guicedinjection.interfaces.IDefaultService;
import com.guicedee.guicedinjection.interfaces.IServiceEnablement;
import com.guicedee.guicedservlets.websockets.options.WebSocketMessageReceiver;

import javax.websocket.Session;

public interface IWebSocketService<J extends IWebSocketService<J>>
		extends IDefaultService<J>, IServiceEnablement<J>
{
	void onOpen(Session session, GuicedWebSocket socket);

	void onClose(Session session, GuicedWebSocket socket);

	void onMessage(String message, Session session, WebSocketMessageReceiver messageReceiver, GuicedWebSocket socket);

	void onError(Throwable t, GuicedWebSocket socket);
}
