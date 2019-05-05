package com.jwebmp.websockets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwebmp.guicedinjection.GuiceContext;
import com.jwebmp.logger.LogFactory;
import com.jwebmp.websockets.options.WebSocketMessageReceiver;
import com.jwebmp.websockets.services.IWebSocketMessageReceiver;
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
public class GuicedWebSocket
{
	@SuppressWarnings("WeakerAccess")
	public static final String EveryoneGroup = "Everyone";

	private static final Logger log = LogFactory.getLog("JWebMPWebSocket");

	private static final Map<String, Set<Session>> groupedSessions = new ConcurrentHashMap<>(5, 2, 1);
	private static final Map<Session, String> webSocketSessionBindings = new ConcurrentHashMap<>(5, 2, 1);

	private static final Map<String, List<IWebSocketMessageReceiver>> messageListeners = new ConcurrentHashMap<>();

	public GuicedWebSocket()
	{
		Set<IWebSocketMessageReceiver> messageReceivers = GuiceContext.instance()
		                                                              .getLoader(IWebSocketMessageReceiver.class, ServiceLoader.load(IWebSocketMessageReceiver.class));

		for (IWebSocketMessageReceiver messageReceiver : messageReceivers)
		{
			for (String messageName : messageReceiver.messageNames())
			{
				if (!messageListeners.containsKey(messageName))
				{
					messageListeners.put(messageName, new ArrayList<>());
				}
				messageListeners.get(messageName)
				                .add(messageReceiver);
				log.log(Level.CONFIG, "Registered new IWebSocketReciever [" + messageReceiver.getClass()
				                                                                             .getCanonicalName() + "]");
			}
		}
	}

	public static void removeFromGroup(String groupName, Session session)
	{
		GuicedWebSocket.getGroup(groupName)
		               .remove(session);
	}

	public static Set<Session> getGroup(String groupName)
	{
		GuicedWebSocket.groupedSessions.computeIfAbsent(groupName, k -> new CopyOnWriteArraySet<>());
		return GuicedWebSocket.groupedSessions.get(groupName);
	}

	public static void remove(String id)
	{
		GuicedWebSocket.groupedSessions.forEach((key, value) ->
				                                        value.removeIf(a -> a.getId()
				                                                             .equals(id)));
		for (Iterator<Map.Entry<Session, String>> iterator = GuicedWebSocket.webSocketSessionBindings.entrySet()
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
		for (IWebSocketSessionProvider sessionProvider : GuiceContext.instance()
		                                                             .getLoader(IWebSocketSessionProvider.class, ServiceLoader.load(IWebSocketSessionProvider.class)))
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
		GuicedWebSocket.addToGroup(GuicedWebSocket.EveryoneGroup, session);
		GuiceContext.instance()
		            .getLoader(IWebSocketService.class, ServiceLoader.load(IWebSocketService.class))
		            .forEach(a -> a.onOpen(session, this));
	}

	public static void addToGroup(String groupName, Session session)
	{
		GuicedWebSocket.getGroup(groupName)
		               .add(session);
	}

	@OnClose
	public void onClose(Session session)
	{
		GuicedWebSocket.remove(session);
		GuiceContext.instance()
		            .getLoader(IWebSocketService.class, ServiceLoader.load(IWebSocketService.class))
		            .forEach(a -> a.onClose(session, this));
	}

	public static void remove(Session session)
	{
		for (Map.Entry<String, Set<Session>> entry : GuicedWebSocket.groupedSessions.entrySet())
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
		GuicedWebSocket.webSocketSessionBindings.remove(session);
	}

	@OnMessage
	public void onMessage(String message, Session session)
	{
		try
		{
			WebSocketMessageReceiver<?> messageReceived = GuiceContext.get(ObjectMapper.class)
			                                                          .readValue(message, WebSocketMessageReceiver.class);
			if (messageReceived.getData()
			                   .get("sessionid") != null)
			{
				GuicedWebSocket.getWebSocketSessionBindings()
				               .put(session, messageReceived.getData()
				                                            .get("sessionid"));
			}
			GuicedWebSocket.log.log(Level.FINE, "Message Received - " + session.getId() + " Message=" + messageReceived.toString());
			GuiceContext.instance()
			            .getLoader(IWebSocketService.class, ServiceLoader.load(IWebSocketService.class))
			            .forEach(a -> a.onMessage(message, session, messageReceived, this));
			Set<IWebSocketMessageReceiver> messageReceivers = GuiceContext.instance()
			                                                              .getLoader(IWebSocketMessageReceiver.class, ServiceLoader.load(IWebSocketMessageReceiver.class));
			for (IWebSocketMessageReceiver messageReceiver : messageReceivers)
			{
				messageReceiver.receiveMessage(messageReceived);
			}
		}
		catch (Exception e)
		{
			GuicedWebSocket.log.log(Level.SEVERE, "ERROR Message Received - " + session.getId() + " Message=" + message, e);
		}
	}

	public static Map<Session, String> getWebSocketSessionBindings()
	{
		return GuicedWebSocket.webSocketSessionBindings;
	}


	public static void broadcastMessage(String groupName, String message)
	{
		GuicedWebSocket.getGroup(groupName)
		               .forEach(a ->
				                        a.getAsyncRemote()
				                         .sendText(message));
	}

	@OnError
	public void onError(Throwable t)
	{
		GuicedWebSocket.log.log(Level.SEVERE, "Error occurred in WebSocket", t);
		GuiceContext.instance()
		            .getLoader(IWebSocketService.class, ServiceLoader.load(IWebSocketService.class))
		            .forEach(a -> a.onError(t, this));
	}
}
