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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint("/")
public class JWebMPSocket
{
	@SuppressWarnings("WeakerAccess")
	public static final String EveryoneGroup = "Everyone";
	private static final Logger log = LogFactory.getLog("JWebMPWebSocket");
	private static final Map<String, Set<Session>> groupedSessions = new ConcurrentHashMap<>(5, 2, 1);
	private static final Map<Session, String> webSocketSessionBindings = new ConcurrentHashMap<>(5, 2, 1);

	private static final ServiceLoader<IWebSocketService> services = ServiceLoader.load(IWebSocketService.class);
	private static final ServiceLoader<IWebSocketSessionProvider> sessionProviders = ServiceLoader.load(IWebSocketSessionProvider.class);

	public JWebMPSocket()
	{
		//No Config Required
	}

	public static void removeFromGroup(String groupName, Session session)
	{
		JWebMPSocket.getGroup(groupName)
		            .remove(session);
	}

	public static Set<Session> getGroup(String groupName)
	{
		JWebMPSocket.groupedSessions.computeIfAbsent(groupName, k -> new CopyOnWriteArraySet<>());
		return JWebMPSocket.groupedSessions.get(groupName);
	}

	public static void remove(String id)
	{
		JWebMPSocket.groupedSessions.forEach((key, value) ->
				                                     value.removeIf(a -> a.getId()
				                                                          .equals(id)));
		for (Iterator<Map.Entry<Session, String>> iterator = JWebMPSocket.webSocketSessionBindings.entrySet()
		                                                                                          .iterator(); iterator.hasNext(); )
		{
			Map.Entry<Session, String> entry = iterator.next();
			Session key = entry.getKey();
			String value = entry.getValue();
			if (value.equals(id))
			{
				iterator.remove();
			}
		}
	}

	/**
	 * Returns a session if valid that is linked to this session
	 *
	 * @param id
	 *
	 * @return
	 */
	public static HttpSession getLinkedSession(String id)
	{
		for (IWebSocketSessionProvider sessionProvider : JWebMPSocket.sessionProviders)
		{
			HttpSession session = sessionProvider.getSession(id);
			if (session != null)
			{
				return session;
			}
		}
		return null;
	}

	@OnOpen
	public void onOpen(Session session)
	{
		JWebMPSocket.addToGroup(JWebMPSocket.EveryoneGroup, session);
		JWebMPSocket.services.forEach(a -> a.onOpen(session, this));
	}

	public static void addToGroup(String groupName, Session session)
	{
		JWebMPSocket.getGroup(groupName)
		            .add(session);
	}

	@OnClose
	public void onClose(Session session)
	{
		JWebMPSocket.remove(session);
		JWebMPSocket.services.forEach(a -> a.onClose(session, this));
	}

	public static void remove(Session session)
	{
		for (Map.Entry<String, Set<Session>> entry : JWebMPSocket.groupedSessions.entrySet())
		{
			List<Session> value = new ArrayList<>(entry.getValue());
			for (int i = 0; i < value.size(); i++)
			{
				if (value.get(i)
				         .getId()
				         .equals(session.getId()))
				{
					value.remove(i);
					break;
				}
			}
		}
		JWebMPSocket.webSocketSessionBindings.remove(session);
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
				JWebMPSocket.getWebSocketSessionBindings()
				            .put(session, messageReceived.getData()
				                                         .get("sessionid"));
			}
			JWebMPSocket.log.log(Level.FINE, "Message Received - " + session.getId() + " Message=" + messageReceived.toString());
			JWebMPSocket.services.forEach(a -> a.onMessage(message, session, messageReceived, this));
		}
		catch (Exception e)
		{
			JWebMPSocket.log.log(Level.SEVERE, "ERROR Message Received - " + session.getId() + " Message=" + message, e);
		}

		try
		{
			JWebMPSocket.broadcastMessage("Everyone", new AjaxResponse());
		}
		catch (Exception e)
		{
			JWebMPSocket.log.log(Level.SEVERE, "ERROR Message Received - " + session.getId() + " Message=" + message, e);
		}
	}

	public static Map<Session, String> getWebSocketSessionBindings()
	{
		return JWebMPSocket.webSocketSessionBindings;
	}

	public static void broadcastMessage(String groupName, AjaxResponse message)
	{
		JWebMPSocket.getGroup(groupName)
		            .forEach(a ->
				                     a.getAsyncRemote()
				                      .sendText(message.toString()));
	}

	@OnError
	public void onError(Throwable t)
	{
		JWebMPSocket.log.log(Level.SEVERE, "Error occurred in WebSocket", t);
		JWebMPSocket.services.forEach(a -> a.onError(t, this));
	}
}
