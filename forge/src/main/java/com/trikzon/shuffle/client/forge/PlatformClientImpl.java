package com.trikzon.shuffle.client.forge;

import com.mojang.blaze3d.platform.InputConstants;
import com.trikzon.shuffle.client.PlatformClient;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PlatformClientImpl {
    private static final List<KeyMapping> keyMappings = new ArrayList<>();

    public static KeyMapping registerKeyMapping(ResourceLocation resLoc, int key, String category) {
        KeyMapping keyMapping = new KeyMapping(
                String.format("key.%s.%s", resLoc.getNamespace(), resLoc.getPath()),
                InputConstants.Type.KEYSYM,
                key,
                category
        );
        keyMappings.add(keyMapping);
        return keyMapping;
    }

    public static void onRegisterKeyMappings(final RegisterKeyMappingsEvent event) {
        for (KeyMapping keyMapping : keyMappings) {
            event.register(keyMapping);
        }
    }

    public static void registerClientTickEvent(Consumer<Minecraft> callback) {
        MinecraftForge.EVENT_BUS.<TickEvent.ClientTickEvent>addListener(e -> {
            callback.accept(Minecraft.getInstance());
        });
    }

    public static void registerRightClickBlockEvent(PlatformClient.RightClickBlockCallback callback) {
        MinecraftForge.EVENT_BUS.<PlayerInteractEvent.RightClickBlock>addListener(e -> {
            callback.rightClickBlock(e.getEntity(), e.getLevel(), e.getHand(), e.getHitVec());
        });
    }
}
