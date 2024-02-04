package com.guicedee.guicedservlets.websockets;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletionStage;

import com.guicedee.guicedservlets.undertow.GuicedUndertow;
import org.junit.jupiter.api.Test;

public class WebsocketClient
{
	@Test
	public void testWebSocketClient() throws Exception
	{
		GuicedUndertow.boot("0.0.0.0",8888);
		
		
		HttpClient.newBuilder()
						.build()
						.newWebSocketBuilder()
						.header("Origin", "https://web.whatsapp.com")
						.header("User-Agent", "some user agent")
						.subprotocols("permessage-deflate", "client_max_window_bits")
						.buildAsync(URI.create("wss://127.0.0.1:8888/wssocket"), new WebSocket.Listener()
						{
							@Override
							public void onOpen(WebSocket webSocket)
							{
								WebSocket.Listener.super.onOpen(webSocket);
							}
							
							@Override
							public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last)
							{
								return WebSocket.Listener.super.onText(webSocket, data, last);
							}
							
							@Override
							public CompletionStage<?> onBinary(WebSocket webSocket, ByteBuffer data, boolean last)
							{
								return WebSocket.Listener.super.onBinary(webSocket, data, last);
							}
							
							@Override
							public CompletionStage<?> onPing(WebSocket webSocket, ByteBuffer message)
							{
								return WebSocket.Listener.super.onPing(webSocket, message);
							}
							
							@Override
							public CompletionStage<?> onPong(WebSocket webSocket, ByteBuffer message)
							{
								return WebSocket.Listener.super.onPong(webSocket, message);
							}
							
							@Override
							public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason)
							{
								return WebSocket.Listener.super.onClose(webSocket, statusCode, reason);
							}
							
							@Override
							public void onError(WebSocket webSocket, Throwable error)
							{
								WebSocket.Listener.super.onError(webSocket, error);
							}
						});
	}
}
