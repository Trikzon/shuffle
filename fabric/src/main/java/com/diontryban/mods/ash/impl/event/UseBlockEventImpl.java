package com.diontryban.mods.ash.impl.event;

import com.diontryban.mods.ash.api.event.UseBlockEvent;

public class UseBlockEventImpl extends UseBlockEvent {
    @Override
    protected void registerImpl(UseBlockCallback callback) {
        net.fabricmc.fabric.api.event.player.UseBlockCallback.EVENT.register(callback::useBlock);
    }
}
