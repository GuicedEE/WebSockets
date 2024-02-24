import com.guicedee.guicedservlets.undertow.services.UndertowPathHandler;
import com.guicedee.guicedservlets.websockets.implementations.GuicedUndertowWebSocketConfiguration;
import com.guicedee.guicedservlets.websockets.implementations.UndertowWebSocketSessionProvider;
import com.guicedee.guicedservlets.websockets.implementations.WebsocketUndertowPathHandler;
import com.guicedee.guicedservlets.websockets.services.IWebSocketService;
import com.guicedee.guicedservlets.websockets.services.IWebSocketSessionProvider;

module com.guicedee.guicedservlets.websockets {
	
	requires undertow.websockets.jsr;
	
	uses com.guicedee.guicedservlets.websockets.services.IWebSocketPreConfiguration;
	uses IWebSocketSessionProvider;
	uses IWebSocketService;
	uses com.guicedee.guicedservlets.websockets.services.IWebSocketMessageReceiver;
	
	requires static lombok;
	
	requires transitive com.guicedee.guicedservlets;
	requires static com.guicedee.guicedservlets.undertow;
	requires static java.net.http;
	
	requires jakarta.websocket;
	requires jakarta.websocket.client;
	requires undertow.core;
	requires undertow.servlet;
	
	exports com.guicedee.guicedservlets.websockets;
	
	provides com.guicedee.guicedinjection.interfaces.IGuicePreStartup with com.guicedee.guicedservlets.websockets.WebSocketsConfiguration;
	provides com.guicedee.guicedservlets.websockets.services.IWebSocketPreConfiguration with GuicedUndertowWebSocketConfiguration;
	provides IWebSocketSessionProvider with UndertowWebSocketSessionProvider;
	provides UndertowPathHandler with WebsocketUndertowPathHandler;
	
	opens com.guicedee.guicedservlets.websockets to undertow.servlet;
}
