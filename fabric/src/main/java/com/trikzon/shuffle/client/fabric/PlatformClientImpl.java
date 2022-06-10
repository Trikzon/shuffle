package com.trikzon.shuffle.client.fabric;

import com.mojang.blaze3d.platform.InputConstants;
import com.trikzon.shuffle.client.PlatformClient;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public class PlatformClientImpl {
    public static KeyMapping registerKeyMapping(ResourceLocation resLoc, int key, String category) {
        return KeyBindingHelper.registerKeyBinding(new KeyMapping(
                String.format("key.%s.%s", resLoc.getNamespace(), resLoc.getPath()),
                InputConstants.Type.KEYSYM,
                key,
                category
        ));
    }

    public static void registerClientTickEvent(Consumer<Minecraft> callback) {
        ClientTickEvents.END_CLIENT_TICK.register(callback::accept);
    }

    public static void registerRightClickBlockEvent(PlatformClient.RightClickBlockCallback callback) {
        UseBlockCallback.EVENT.register(callback::rightClickBlock);
    }
}
