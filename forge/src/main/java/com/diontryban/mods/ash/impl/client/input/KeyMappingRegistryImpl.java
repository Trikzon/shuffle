package com.diontryban.mods.ash.impl.client.input;

import com.diontryban.mods.ash.api.client.input.KeyMappingRegistry;
import com.diontryban.mods.ash.api.forge.ModEventBus;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyMappingRegistryImpl extends KeyMappingRegistry {
    private static final Map<String, List<KeyMapping>> MOD_KEY_MAPPINGS = new HashMap<>();

    @Override
    protected KeyMapping registerKeyMappingImpl(String modId, KeyMapping keyMapping) {
        if (!MOD_KEY_MAPPINGS.containsKey(modId)) {
            MOD_KEY_MAPPINGS.put(modId, new ArrayList<>());

            ModEventBus.getOrThrow(modId).<RegisterKeyMappingsEvent>addListener(event -> {
                for (KeyMapping key : MOD_KEY_MAPPINGS.get(modId)) {
                    event.register(key);
                }
            });
        }

        MOD_KEY_MAPPINGS.get(modId).add(keyMapping);

        return keyMapping;
    }
}
