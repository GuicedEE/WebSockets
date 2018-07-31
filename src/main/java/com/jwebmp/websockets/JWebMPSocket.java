package com.jwebmp.websockets;

import com.jwebmp.core.base.ajax.AjaxResponse;
import com.jwebmp.core.htmlbuilder.javascript.JavaScriptPart;
import com.jwebmp.logger.LogFactory;
import com.jwebmp.websockets.options.WebSocketMessageReceiver;
import com.jwebmp.websockets.services.IWebSocketService;
import com.jwebmp.websockets.services.IWebSocketSessionProvider;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.HashSet;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint("/")
public class JWebMPSocket
{
	private static final Logger log = LogFactory.getLog("JWebMPWebSocket");

	private static final Map<String, Set<Session>> groupedSessions = new ConcurrentHashMap<>();
	private static final Map<Session, String> webSocketSessionBindings = new ConcurrentHashMap<>();

	@SuppressWarnings("WeakerAccess")
	public static final String EveryoneGroup = "Everyone";

	private static final ServiceLoader<IWebSocketService> services = ServiceLoader.load(IWebSocketService.class);
	private static final ServiceLoader<IWebSocketSessionProvider> sessionProviders = ServiceLoader.load(IWebSocketSessionProvider.class);

	public JWebMPSocket()
	{
		//No Config Required
	}

	public static void addToGroup(String groupName, Session session)
	{
		getGroup(groupName).add(session);
	}

	public static void removeFromGroup(String groupName, Session session)
	{
		getGroup(groupName).remove(session);
	}

	public static void remove(Session session)
	{
		groupedSessions.forEach((key, value) ->
				                        value.removeIf(a -> a.equals(session)));
		webSocketSessionBindings.remove(session);
	}

	public static void remove(String id)
	{
		groupedSessions.forEach((key, value) ->
				                        value.removeIf(a -> a.getId()
				                                             .equals(id)));
		webSocketSessionBindings.forEach((key, value) -> value.equals(id));
	}

	public static Set<Session> getGroup(String groupName)
	{
		groupedSessions.computeIfAbsent(groupName, k -> new HashSet<>());
		return groupedSessions.get(groupName);
	}

	public static void broadcastMessage(String groupName, AjaxResponse message)
	{
		getGroup(groupName).forEach(a ->
				                            a.getAsyncRemote()
				                             .sendText(message.toString()));
	}

	@OnOpen
	public void onOpen(Session session)
	{
		addToGroup(EveryoneGroup, session);
		services.forEach(a -> a.onOpen(session, this));
	}

	@OnClose
	public void onClose(Session session)
	{
		remove(session);
		services.forEach(a -> a.onClose(session, this));
	}

	@OnMessage
	public void onMessage(String message, Session session)
	{
		try
		{
			WebSocketMessageReceiver<?> messageReceived = new JavaScriptPart<>().From(message, WebSocketMessageReceiver.class);
			if (messageReceived.getData()
			                   .get("sessionid") != null)
			{
				getWebSocketSessionBindings().put(session, messageReceived.getData()
				                                                          .get("sessionid"));
			}
			log.log(Level.FINE, "Message Received - " + session.getId() + " Message=" + messageReceived);
			services.forEach(a -> a.onMessage(message, session, messageReceived, this));
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "ERROR Message Received - " + session.getId() + " Message=" + message, e);
		}

		try
		{
			broadcastMessage("Everyone", new AjaxResponse());
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "ERROR Message Received - " + session.getId() + " Message=" + message, e);
		}
	}

	@OnError
	public void onError(Throwable t)
	{
		log.log(Level.SEVERE, "Error occurred in WebSocket", t);
		services.forEach(a -> a.onError(t, this));
	}

	public static Map<Session, String> getWebSocketSessionBindings()
	{
		return webSocketSessionBindings;
	}

	/**
	 * Returns a session if valid that is linked to this session
	 * @param id
	 * @return
	 */
	public static HttpSession getLinkedSession(String id)
	{
		for (IWebSocketSessionProvider sessionProvider : sessionProviders)
		{
			HttpSession session = sessionProvider.getSession(id);
			if (session != null)
			{
				return session;
			}
		}
		return null;
	}
}
