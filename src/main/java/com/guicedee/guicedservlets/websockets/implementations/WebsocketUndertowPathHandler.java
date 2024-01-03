package com.guicedee.guicedservlets.websockets.implementations;

import com.guicedee.guicedservlets.undertow.services.UndertowPathHandler;
import io.undertow.server.HttpHandler;
import jakarta.annotation.Nullable;

import static io.undertow.Handlers.path;

public class WebsocketUndertowPathHandler implements UndertowPathHandler<WebsocketUndertowPathHandler>
{
	@Override
	public HttpHandler registerPathHandler(@Nullable HttpHandler incoming)
	{
		path().addPrefixPath("/wssocket", GuicedUndertowWebSocketConfiguration.getWebSocketHandler());
		return incoming;
	}
}
