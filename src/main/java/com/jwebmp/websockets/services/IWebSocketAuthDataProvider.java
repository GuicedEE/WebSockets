package com.jwebmp.websockets.services;

/**
 * Service to load authentication data for web service
 */
public interface IWebSocketAuthDataProvider
{
	StringBuilder getJavascriptToPopulate();
	String name();
}
