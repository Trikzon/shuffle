package com.diontryban.mods.ash.api.forge;

import net.minecraftforge.eventbus.api.IEventBus;

import java.util.HashMap;
import java.util.Optional;

public class ModEventBus {
    private static final HashMap<String, IEventBus> MOD_EVENT_BUSES = new HashMap<>();

    public static void register(String modId, IEventBus bus) {
        MOD_EVENT_BUSES.put(modId, bus);
    }

    public static Optional<IEventBus> getOptional(String modId) {
        return Optional.ofNullable(MOD_EVENT_BUSES.get(modId));
    }

    public static IEventBus getOrThrow(String modId) {
        return getOptional(modId).orElseThrow(
                () -> new NullPointerException("Mod Event Bus for " + modId + " has not been registered.")
        );
    }
}
