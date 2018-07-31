package com.jwebmp.websockets.services;
import javax.servlet.http.HttpSession;

/**
 * Returns the HTTPSession associated with the given id
 */
public interface IWebSocketSessionProvider
{
	HttpSession getSession(String sessionID);
}
