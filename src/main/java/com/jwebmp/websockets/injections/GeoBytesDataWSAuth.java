package com.jwebmp.websockets.injections;

import com.jwebmp.websockets.services.IWebSocketAuthDataProvider;

public class GeoBytesDataWSAuth
		implements IWebSocketAuthDataProvider<GeoBytesDataWSAuth>
{
	@Override
	public StringBuilder getJavascriptToPopulate()
	{
		return new StringBuilder(" $.getJSON('http://gd.geobytes.com/GetCityDetails?callback=?', function(data) {\n" +
		                         "        data.localstorage = jw.localstorage['jwamsmk'];\n" +
		                         "        jw.websocket.newMessage('Auth',data);\n" +
		                         "    });");
	}

	@Override
	public String name()
	{
		return "JWebMPGeoBytesAuthData";
	}

	@Override
	public boolean enabled()
	{
		return WebSocketsConfiguration.isGeoBytesEnabled();
	}
}
