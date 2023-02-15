package com.diontryban.mods.ash.impl.client.input;

import com.diontryban.mods.ash.api.client.input.KeyMappingRegistry;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;

public class KeyMappingRegistryImpl extends KeyMappingRegistry {
    @Override
    protected KeyMapping registerKeyMappingImpl(String modId, KeyMapping keyMapping) {
        return KeyBindingHelper.registerKeyBinding(keyMapping);
    }
}
