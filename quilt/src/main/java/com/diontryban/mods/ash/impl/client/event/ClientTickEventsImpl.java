package com.diontryban.mods.ash.impl.client.event;

import com.diontryban.mods.ash.api.client.event.ClientTickEvents;

public class ClientTickEventsImpl extends ClientTickEvents {
    @Override
    protected void registerStartImpl(Start callback) {
        org.quiltmc.qsl.lifecycle.api.client.event.ClientTickEvents.START.register(callback::startClientTick);
    }

    @Override
    protected void registerEndImpl(End callback) {
        org.quiltmc.qsl.lifecycle.api.client.event.ClientTickEvents.END.register(callback::endClientTick);
    }
}
