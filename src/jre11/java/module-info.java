import com.guicedee.guicedinjection.interfaces.IGuicePreStartup;
import com.guicedee.guicedinjection.interfaces.IGuiceScanJarExclusions;
import com.guicedee.guicedinjection.interfaces.IGuiceScanModuleExclusions;
import com.guicedee.guicedservlets.websockets.WebSocketsConfiguration;
import com.guicedee.guicedservlets.websockets.injections.WebSocketModuleExclusions;
import com.guicedee.guicedservlets.websockets.services.IWebSocketMessageReceiver;
import com.guicedee.guicedservlets.websockets.services.IWebSocketPreConfiguration;
import com.guicedee.guicedservlets.websockets.services.IWebSocketService;
import com.guicedee.guicedservlets.websockets.services.IWebSocketSessionProvider;

module com.guicedee.guicedservlets.websockets {

	uses IWebSocketPreConfiguration;
	uses IWebSocketSessionProvider;
	uses IWebSocketService;
	uses IWebSocketMessageReceiver;

	requires javax.websocket.api;
	requires java.logging;


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

	provides IGuicePreStartup with WebSocketsConfiguration;
	provides IGuiceScanModuleExclusions with WebSocketModuleExclusions;
	provides IGuiceScanJarExclusions with WebSocketModuleExclusions;

	opens com.guicedee.guicedservlets.websockets;
	opens com.guicedee.guicedservlets.websockets.services;

	opens com.guicedee.guicedservlets.websockets.options to com.fasterxml.jackson.databind;
}
