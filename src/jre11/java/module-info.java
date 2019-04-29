module com.jwebmp.websockets {

	uses com.jwebmp.websockets.services.IWebSocketPreConfiguration;
	uses com.jwebmp.websockets.services.IWebSocketSessionProvider;
	uses com.jwebmp.websockets.services.IWebSocketService;

	requires javax.websocket.api;
	requires java.logging;


	requires com.jwebmp.logmaster;
	requires com.jwebmp.guicedinjection;

	requires com.fasterxml.jackson.annotation;

	requires javax.servlet.api;

	requires java.validation;
	requires com.fasterxml.jackson.databind;

	exports com.jwebmp.websockets;
	exports com.jwebmp.websockets.services;

	exports com.jwebmp.websockets.injections to com.jwebmp.guicedinjection, com.google.guice;

	provides com.jwebmp.guicedinjection.interfaces.IGuicePreStartup with com.jwebmp.websockets.WebSocketsConfiguration;
	provides com.jwebmp.guicedinjection.interfaces.IGuiceScanModuleExclusions with com.jwebmp.websockets.injections.WebSocketModuleExclusions;
	provides com.jwebmp.guicedinjection.interfaces.IGuiceScanJarExclusions with com.jwebmp.websockets.injections.WebSocketModuleExclusions;

	opens com.jwebmp.websockets;
	opens com.jwebmp.websockets.services;

	opens com.jwebmp.websockets.options to com.fasterxml.jackson.databind;
}
