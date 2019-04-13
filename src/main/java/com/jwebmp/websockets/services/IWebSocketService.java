package com.jwebmp.websockets.services;

import com.jwebmp.guicedinjection.interfaces.IDefaultService;
import com.jwebmp.guicedinjection.interfaces.IServiceEnablement;
import com.jwebmp.websockets.GuicedWebSocket;
import com.jwebmp.websockets.options.WebSocketMessageReceiver;

import javax.websocket.Session;

public interface IWebSocketService<J extends IWebSocketService<J>>
		extends IDefaultService<J>, IServiceEnablement<J>
{
	void onOpen(Session session, GuicedWebSocket socket);

	void onClose(Session session, GuicedWebSocket socket);

	void onMessage(String message, Session session, WebSocketMessageReceiver messageReceiver, GuicedWebSocket socket);

	void onError(Throwable t, GuicedWebSocket socket);
}
