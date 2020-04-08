module com.guicedee.guicedservlets.websockets {

	uses com.guicedee.guicedservlets.websockets.services.IWebSocketPreConfiguration;
	uses com.guicedee.guicedservlets.websockets.services.IWebSocketSessionProvider;
	uses com.guicedee.guicedservlets.websockets.services.IWebSocketService;
	uses com.guicedee.guicedservlets.websockets.services.IWebSocketMessageReceiver;

	requires transitive com.guicedee.guicedservlets;

	requires javax.websocket.api;
	requires java.logging;

	requires com.fasterxml.jackson.annotation;
	requires transitive java.validation;

	exports com.guicedee.guicedservlets.websockets;
	exports com.guicedee.guicedservlets.websockets.options;
	exports com.guicedee.guicedservlets.websockets.services;

	exports com.guicedee.guicedservlets.websockets.injections to com.guicedee.guicedinjection, com.google.guice;

	provides com.guicedee.guicedinjection.interfaces.IGuicePreStartup with com.guicedee.guicedservlets.websockets.WebSocketsConfiguration;

	opens com.guicedee.guicedservlets.websockets;
	opens com.guicedee.guicedservlets.websockets.services;

	opens com.guicedee.guicedservlets.websockets.options to com.fasterxml.jackson.databind;
}
