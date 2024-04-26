package com.guicedee.guicedservlets.websockets.implementations;

import com.guicedee.guicedservlets.undertow.services.UndertowPathHandler;
import io.undertow.server.HttpHandler;

import java.util.Map;

public class WebsocketUndertowPathHandler implements UndertowPathHandler<WebsocketUndertowPathHandler>
{
	@Override
	public Map<String, HttpHandler> registerPathHandler()
	{
		return Map.of("/wssocket", GuicedUndertowWebSocketConfiguration.getWebSocketHandler());
	}
}
