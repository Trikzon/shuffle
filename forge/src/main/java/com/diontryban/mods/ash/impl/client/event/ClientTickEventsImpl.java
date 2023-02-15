package com.diontryban.mods.ash.impl.client.event;

import com.diontryban.mods.ash.api.client.event.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;

public class ClientTickEventsImpl extends ClientTickEvents {
    @Override
    protected void registerStartImpl(Start callback) {
        MinecraftForge.EVENT_BUS.<TickEvent.ClientTickEvent>addListener(event -> {
            if (event.phase == TickEvent.Phase.START) {
                callback.startClientTick(Minecraft.getInstance());
            }
        });
    }

    @Override
    protected void registerEndImpl(End callback) {
        MinecraftForge.EVENT_BUS.<TickEvent.ClientTickEvent>addListener(event -> {
            if (event.phase == TickEvent.Phase.END) {
                callback.endClientTick(Minecraft.getInstance());
            }
        });
    }
}
