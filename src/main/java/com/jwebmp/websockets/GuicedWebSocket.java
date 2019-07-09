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
	private static final Map<String, Session> webSocketSessionBindings = new ConcurrentHashMap<>(5, 2, 1);
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
		getGroup(groupName)
				.remove(session);
	}

	public static Set<Session> getGroup(String groupName)
	{
		groupedSessions.computeIfAbsent(groupName, k -> new CopyOnWriteArraySet<>());
		return groupedSessions.get(groupName);
	}

	public static void remove(String id)
	{
		groupedSessions.forEach((key, value) ->
				                        value.removeIf(a -> a.getId()
				                                             .equals(id)));

		for (Iterator<Map.Entry<String, Session>> iterator = webSocketSessionBindings.entrySet()
		                                                                             .iterator(); iterator.hasNext(); )
		{
			Map.Entry<String, Session> entry = iterator.next();
			String key = entry.getKey();
			if (key.equals(id))
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
		addToGroup(EveryoneGroup, session);
		GuiceContext.instance()
		            .getLoader(IWebSocketService.class, ServiceLoader.load(IWebSocketService.class))
		            .forEach(a -> a.onOpen(session, this));
	}

	public static void addToGroup(String groupName, Session session)
	{
		getGroup(groupName)
				.add(session);
	}

	@OnClose
	public void onClose(Session session)
	{
		remove(session);
		GuiceContext.instance()
		            .getLoader(IWebSocketService.class, ServiceLoader.load(IWebSocketService.class))
		            .forEach(a -> a.onClose(session, this));
		log.config("Removed web socket session -" + session);
	}

	public static void remove(Session session)
	{
		for (Map.Entry<String, Set<Session>> entry : groupedSessions.entrySet())
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
			if (value.isEmpty())
			{
				groupedSessions.remove(entry.getKey());
			}
		}
		for (Iterator<Map.Entry<String, Session>> iterator = webSocketSessionBindings.entrySet()
		                                                                             .iterator(); iterator.hasNext(); )
		{
			Map.Entry<String, Session> entry = iterator.next();
			Session value = entry.getValue();
			if (value.equals(session))
			{
				iterator.remove();
			}
		}
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
				getWebSocketSessionBindings()
						.put(messageReceived.getData()
						                    .get("sessionid"), session);
				addToGroup(messageReceived.getData()
				                          .get("sessionid"), session);
			}
			log.log(Level.FINER, "Web Socket Message Received - " + session.getId() + " Message=" + messageReceived.toString());
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
			log.log(Level.SEVERE, "ERROR Message Received - " + session.getId() + " Message=" + message, e);
		}
	}

	public static Map<String, Session> getWebSocketSessionBindings()
	{
		return webSocketSessionBindings;
	}


	public static void broadcastMessage(String groupName, String message)
	{
		getGroup(groupName)
				.forEach(a ->
						         a.getAsyncRemote()
						          .sendText(message));
	}

	@OnError
	public void onError(Throwable t, Session session)
	{
		log.log(Level.SEVERE, "Error occurred in WebSocket", t);
		GuiceContext.instance()
		            .getLoader(IWebSocketService.class, ServiceLoader.load(IWebSocketService.class))
		            .forEach(a -> a.onError(t, this));
		remove(session);
		log.config("Removed web socket session -" + session);
	}
}
