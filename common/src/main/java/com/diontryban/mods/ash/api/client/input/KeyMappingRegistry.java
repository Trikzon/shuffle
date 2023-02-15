package com.diontryban.mods.ash.api.client.input;

import com.diontryban.mods.ash.ImplementationLoader;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;

public abstract class KeyMappingRegistry {
    private static final KeyMappingRegistry IMPL = ImplementationLoader.load(KeyMappingRegistry.class);

    public static KeyMapping registerKeyMapping(String modId, KeyMapping keyMapping) {
        return IMPL.registerKeyMappingImpl(modId, keyMapping);
    }

    public static KeyMapping registerKeyMapping(
            ResourceLocation resLoc,
            InputConstants.Type inputType,
            int key,
            String category
    ) {
        return registerKeyMapping(resLoc.getNamespace(), new KeyMapping(
                String.format("key.%s.%s", resLoc.getNamespace(), resLoc.getPath()),
                inputType,
                key,
                category
        ));
    }

    public static KeyMapping registerKeyMapping(ResourceLocation resLoc, int key, String category) {
        return registerKeyMapping(resLoc, InputConstants.Type.KEYSYM, key, category);
    }

    protected abstract KeyMapping registerKeyMappingImpl(String modId, KeyMapping keyMapping);
}
