module com.guicedee.guicedservlets.websockets {
	
	uses com.guicedee.guicedservlets.websockets.services.IWebSocketPreConfiguration;
	uses com.guicedee.guicedservlets.websockets.services.IWebSocketSessionProvider;
	uses com.guicedee.guicedservlets.websockets.services.IWebSocketService;
	uses com.guicedee.guicedservlets.websockets.services.IWebSocketMessageReceiver;
	
	requires static lombok;
	
	requires transitive com.guicedee.guicedservlets;
	
	requires jakarta.websocket;
	requires jakarta.websocket.client;
	
	exports com.guicedee.guicedservlets.websockets;
	exports com.guicedee.guicedservlets.websockets.options;
	exports com.guicedee.guicedservlets.websockets.services;
	
	provides com.guicedee.guicedinjection.interfaces.IGuicePreStartup with com.guicedee.guicedservlets.websockets.WebSocketsConfiguration;
	
	opens com.guicedee.guicedservlets.websockets;
	opens com.guicedee.guicedservlets.websockets.services;
	
	opens com.guicedee.guicedservlets.websockets.options to com.fasterxml.jackson.databind;
}
