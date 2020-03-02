module com.guicedee.guicedservlets.websockets {

	uses com.guicedee.guicedservlets.websockets.services.IWebSocketPreConfiguration;
	uses com.guicedee.guicedservlets.websockets.services.IWebSocketSessionProvider;
	uses com.guicedee.guicedservlets.websockets.services.IWebSocketService;
	uses com.guicedee.guicedservlets.websockets.services.IWebSocketMessageReceiver;

	requires javax.websocket.api;
	requires java.logging;

	requires com.google.guice;

	requires com.guicedee.logmaster;
	requires com.guicedee.guicedinjection;

	requires com.fasterxml.jackson.annotation;

	requires javax.servlet.api;

	requires java.validation;
	requires com.fasterxml.jackson.databind;

	exports com.guicedee.guicedservlets.websockets;
	exports com.guicedee.guicedservlets.websockets.options;
	exports com.guicedee.guicedservlets.websockets.services;

	exports com.guicedee.guicedservlets.websockets.injections to com.guicedee.guicedinjection, com.google.guice;

	provides com.guicedee.guicedinjection.interfaces.IGuicePreStartup with com.guicedee.guicedservlets.websockets.WebSocketsConfiguration;

	opens com.guicedee.guicedservlets.websockets;
	opens com.guicedee.guicedservlets.websockets.services;

	opens com.guicedee.guicedservlets.websockets.options to com.fasterxml.jackson.databind;
}
