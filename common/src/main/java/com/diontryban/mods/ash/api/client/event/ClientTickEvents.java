package com.diontryban.mods.ash.api.client.event;

import com.diontryban.mods.ash.ImplementationLoader;
import net.minecraft.client.Minecraft;

public abstract class ClientTickEvents {
    private static final ClientTickEvents IMPL = ImplementationLoader.load(ClientTickEvents.class);

    public static void registerStart(Start callback) {
        IMPL.registerStartImpl(callback);
    }

    public static void registerEnd(End callback) {
        IMPL.registerEndImpl(callback);
    }

    @FunctionalInterface
    public interface Start {
        void startClientTick(Minecraft client);
    }

    @FunctionalInterface
    public interface End {
        void endClientTick(Minecraft client);
    }

    protected abstract void registerStartImpl(Start callback);
    protected abstract void registerEndImpl(End callback);
}
