package com.guicedee.guicedservlets.websockets.implementations;

import com.google.inject.AbstractModule;
import com.guicedee.guicedinjection.interfaces.IGuiceModule;
import com.guicedee.guicedservlets.websockets.GuicedWebSocket;
import com.guicedee.guicedservlets.websockets.options.IGuicedWebSocket;

public class GuicedServletWebSocketsModule extends AbstractModule implements IGuiceModule<GuicedServletWebSocketsModule>
{
    @Override
    protected void configure()
    {
        bind(IGuicedWebSocket.class).to(GuicedWebSocket.class);
    }
}
