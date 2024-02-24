package com.guicedee.guicedservlets.websockets.implementations;

import com.guicedee.guicedservlets.undertow.services.UndertowPathHandler;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import jakarta.annotation.Nullable;

import java.util.Map;

import static io.undertow.Handlers.path;

public class WebsocketUndertowPathHandler implements UndertowPathHandler<WebsocketUndertowPathHandler>
{
	@Override
	public Map<String, HttpHandler> registerPathHandler()
	{
		return Map.of("/wssocket", GuicedUndertowWebSocketConfiguration.getWebSocketHandler());
	}
}
