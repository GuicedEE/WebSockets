import com.jwebmp.core.base.angular.services.IAngularControllerScopeStatement;
import com.jwebmp.guicedinjection.interfaces.IGuicePreStartup;
import com.jwebmp.guicedinjection.interfaces.IGuiceScanJarExclusions;
import com.jwebmp.guicedinjection.interfaces.IGuiceScanModuleExclusions;
import com.jwebmp.websockets.injections.LocalStorageKeyWSAuth;
import com.jwebmp.websockets.injections.WebSocketControllerStatement;
import com.jwebmp.websockets.injections.WebSocketModuleExclusions;
import com.jwebmp.websockets.injections.WebSocketsConfiguration;
import com.jwebmp.websockets.services.IWebSocketAuthDataProvider;

module com.jwebmp.websockets {
	uses com.jwebmp.websockets.services.IWebSocketAuthDataProvider;
	uses com.jwebmp.websockets.services.IWebSocketPreConfiguration;
	uses com.jwebmp.websockets.services.IWebSocketSessionProvider;
	uses com.jwebmp.websockets.services.IWebSocketService;

	requires javax.websocket.api;
	requires java.logging;
	requires com.jwebmp.logmaster;
	requires com.jwebmp.guicedinjection;
	requires com.jwebmp.core;
	requires com.fasterxml.jackson.annotation;
	requires javax.servlet.api;
	requires java.validation;
	requires com.jwebmp.core.angularjs;

	exports com.jwebmp.websockets;
	exports com.jwebmp.websockets.services;

	exports com.jwebmp.websockets.injections to com.jwebmp.guicedinjection, com.google.guice;

	provides IGuicePreStartup with WebSocketsConfiguration;
	provides IAngularControllerScopeStatement with WebSocketControllerStatement;
	provides IWebSocketAuthDataProvider with LocalStorageKeyWSAuth;

	provides IGuiceScanModuleExclusions with WebSocketModuleExclusions;
	provides IGuiceScanJarExclusions with WebSocketModuleExclusions;

	opens com.jwebmp.websockets;
	opens com.jwebmp.websockets.services;

	opens com.jwebmp.websockets.options to com.fasterxml.jackson.databind;
}
