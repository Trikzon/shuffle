package com.diontryban.mods.ash.impl.client.event;

import com.diontryban.mods.ash.api.client.event.ClientTickEvents;

public class ClientTickEventsImpl extends ClientTickEvents {
    @Override
    protected void registerStartImpl(Start callback) {
        net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.START_CLIENT_TICK.register(callback::startClientTick);
    }

    @Override
    protected void registerEndImpl(End callback) {
        net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.END_CLIENT_TICK.register(callback::endClientTick);
    }
}
