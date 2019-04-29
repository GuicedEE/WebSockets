package com.jwebmp.websockets.options;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
//import com.jwebmp.core.htmlbuilder.javascript.JavaScriptPart;

import java.util.HashMap;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.*;

@JsonAutoDetect(fieldVisibility = ANY,
		getterVisibility = NONE,
		setterVisibility = NONE)
@JsonInclude(NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebSocketMessageReceiver<J extends WebSocketMessageReceiver<J>>
		//extends JavaScriptPart<J>
{
	private WebSocketMessageReceiverActionType action;
	private String broadcastGroup;

	private Map<String, String> data = new HashMap<>();

	public WebSocketMessageReceiver()
	{
		//No Config Required
	}

	public WebSocketMessageReceiver(WebSocketMessageReceiverActionType action, Map<String, String> data)
	{
		this.action = action;
		this.data = data;
	}

	public WebSocketMessageReceiverActionType getAction()
	{
		return action;
	}

	@SuppressWarnings("unchecked")
	public J setAction(WebSocketMessageReceiverActionType action)
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
}
