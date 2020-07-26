package com.guicedee.guicedservlets.websockets.options;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
//import com.guicedee.core.htmlbuilder.javascript.JavaScriptPart;

import java.util.HashMap;
import java.util.Map;

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
	private Map<String, String> data = new HashMap<>();

	public WebSocketMessageReceiver()
	{
		//No Config Required
	}

	public WebSocketMessageReceiver(String action, Map<String, String> data)
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

	public Map<String, String> getData()
	{
		return data;
	}

	@SuppressWarnings("unchecked")
	public J setData(Map<String, String> data)
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
