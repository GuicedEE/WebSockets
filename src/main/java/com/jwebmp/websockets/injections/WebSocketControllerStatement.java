/*
 * Copyright (C) 2017 Marc Magon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jwebmp.websockets.injections;

import com.jwebmp.core.FileTemplates;
import com.jwebmp.core.base.angular.services.IAngularControllerScopeStatement;
import com.jwebmp.websockets.services.IWebSocketPreConfiguration;
import com.jwebmp.websockets.services.IWebSocketAuthDataProvider;

import java.util.ServiceLoader;

/**
 * Registers the moment angular module as available for the application
 *
 * @author GedMarc
 * @version 1.0
 * @since Oct 4, 2016
 */
public class WebSocketControllerStatement
		implements IAngularControllerScopeStatement<WebSocketControllerStatement>
{
	private static final ServiceLoader<IWebSocketAuthDataProvider> authDataProviders = ServiceLoader.load(IWebSocketAuthDataProvider.class);

	public WebSocketControllerStatement()
	{
		//No config Required
	}

	@Override
	public StringBuilder getStatement()
	{
		StringBuilder template = FileTemplates.getFileTemplate(IWebSocketPreConfiguration.class, "JW_SCOPE_INSERTIONS", "websockets.js");
		StringBuilder replaceable = new StringBuilder();
		for (IWebSocketAuthDataProvider a : authDataProviders)
		{
			replaceable.append("jw.websocket.authdataproviders.push({name:'" + a.name() + "',");
			replaceable.append("data:" + a.getJavascriptToPopulate() + "});");
		}
		template = new StringBuilder(template.toString()
		                                     .replace("WS_AUTH_DATA_PROVIDER_LOAD;", replaceable.toString()));
		return template;
	}

	@Override
	public String getReferenceName()
	{
		return "WebSocketControllerStatement";
	}

	@Override
	public String renderFunction()
	{
		return null;
	}
}
