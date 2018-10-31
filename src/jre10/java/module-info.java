import com.jwebmp.core.base.angular.services.IAngularControllerScopeStatement;
import com.jwebmp.guicedinjection.interfaces.IGuicePreStartup;
import com.jwebmp.websockets.injections.LocalStorageKeyWSAuth;
import com.jwebmp.websockets.injections.WebSocketControllerStatement;
import com.jwebmp.websockets.injections.WebSocketsConfiguration;
import com.jwebmp.websockets.services.IWebSocketAuthDataProvider;

module com.jwebmp.websockets {
	uses com.jwebmp.websockets.services.IWebSocketAuthDataProvider;
	uses com.jwebmp.websockets.services.IWebSocketPreConfiguration;
	uses com.jwebmp.websockets.services.IWebSocketSessionProvider;
	uses com.jwebmp.websockets.services.IWebSocketService;

	requires transitive javax.websocket.api;

	requires transitive com.jwebmp.core;

	exports com.jwebmp.websockets;
	exports com.jwebmp.websockets.services;


	exports com.jwebmp.websockets.injections to com.google.guice;

	provides IGuicePreStartup with WebSocketsConfiguration;
	provides IAngularControllerScopeStatement with WebSocketControllerStatement;
	provides IWebSocketAuthDataProvider with LocalStorageKeyWSAuth;

	opens com.jwebmp.websockets.services to com.google.guice, com.jwebmp.core;
	opens com.jwebmp.websockets.options to com.fasterxml.jackson.databind;
}
