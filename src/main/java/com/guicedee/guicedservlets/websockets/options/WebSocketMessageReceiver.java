package com.guicedee.guicedservlets.websockets.options;

import com.fasterxml.jackson.annotation.*;
import jakarta.websocket.*;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.*;

@SuppressWarnings("unused")
@JsonAutoDetect(fieldVisibility = ANY,
                getterVisibility = NONE,
                setterVisibility = NONE)
@JsonInclude(NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebSocketMessageReceiver<J extends WebSocketMessageReceiver<J>>
{
	private String action;
	private String broadcastGroup;
	private Map<String, Object> data = new HashMap<>();
	private Session session;
	
	public WebSocketMessageReceiver()
	{
		//No Config Required
	}
	
	public WebSocketMessageReceiver(String action, Map<String, Object> data)
	{
		this.action = action;
		this.data = data;
	}
	
	public String getAction()
	{
		return action;
	}
	
	@SuppressWarnings("unchecked")
	public J setAction(String action)
	{
		this.action = action;
		return (J) this;
	}
	
	public Map<String, Object> getMap(String key)
	{
		return (Map) (getData().get(key) == null ? new LinkedHashMap<>() : getData().get(key));
	}
	
	public Map<String, Object> getData()
	{
		return data;
	}
	
	@SuppressWarnings("unchecked")
	public J setData(Map<String, Object> data)
	{
		this.data = data;
		return (J) this;
	}
	
	public String getBroadcastGroup()
	{
		return broadcastGroup;
	}
	
	public WebSocketMessageReceiver<J> setBroadcastGroup(String broadcastGroup)
	{
		this.broadcastGroup = broadcastGroup;
		return this;
	}
	
	public Session getSession()
	{
		return session;
	}
	
	public WebSocketMessageReceiver<J> setSession(Session session)
	{
		this.session = session;
		return this;
	}
	
	@JsonAnySetter
	public void add(String key, String value)
	{
		data.put(key, value);
	}
	
	@Override
	public String toString()
	{
		return "WebSocketMessageReceiver{" +
		       "action=" + action +
		       ", broadcastGroup='" + broadcastGroup + '\'' +
		       ", data=" + data +
		       '}';
	}
}
