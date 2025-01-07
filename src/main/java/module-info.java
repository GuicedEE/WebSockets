import com.guicedee.guicedinjection.interfaces.IGuiceModule;
import com.guicedee.guicedservlets.undertow.services.UndertowPathHandler;
import com.guicedee.guicedservlets.websockets.implementations.GuicedServletWebSocketsModule;
import com.guicedee.guicedservlets.websockets.implementations.GuicedUndertowWebSocketConfiguration;
import com.guicedee.guicedservlets.websockets.implementations.WebsocketUndertowPathHandler;
module com.guicedee.websockets {
	
	requires undertow.websockets.jsr;
	
	uses com.guicedee.guicedservlets.websockets.services.IWebSocketPreConfiguration;

	uses com.guicedee.guicedservlets.websockets.services.IWebSocketMessageReceiver;
	
	requires static lombok;
	
	requires transitive com.guicedee.guicedservlets;
	requires static java.net.http;
	
	requires jakarta.websocket;
	requires jakarta.websocket.client;
	requires undertow.core;
	requires undertow.servlet;
	
	exports com.guicedee.guicedservlets.websockets;
	
	provides com.guicedee.guicedinjection.interfaces.IGuicePreStartup with com.guicedee.guicedservlets.websockets.WebSocketsConfiguration;
	provides com.guicedee.guicedservlets.websockets.services.IWebSocketPreConfiguration with GuicedUndertowWebSocketConfiguration;
	provides UndertowPathHandler with WebsocketUndertowPathHandler;
	provides IGuiceModule with GuicedServletWebSocketsModule;
	
	opens com.guicedee.guicedservlets.websockets to undertow.servlet;
}
