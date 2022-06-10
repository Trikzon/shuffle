package com.trikzon.shuffle.client.forge;

import com.mojang.blaze3d.platform.InputConstants;
import com.trikzon.shuffle.client.PlatformClient;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

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

    public static void onClientSetup(final FMLClientSetupEvent event) {
        for (KeyMapping keyMapping : keyMappings) {
            ClientRegistry.registerKeyBinding(keyMapping);
        }
    }

    public static void registerClientTickEvent(Consumer<Minecraft> callback) {
        MinecraftForge.EVENT_BUS.<TickEvent.ClientTickEvent>addListener(e -> {
            callback.accept(Minecraft.getInstance());
        });
    }

    public static void registerRightClickBlockEvent(PlatformClient.RightClickBlockCallback callback) {
        MinecraftForge.EVENT_BUS.<PlayerInteractEvent.RightClickBlock>addListener(e -> {
            callback.rightClickBlock(e.getPlayer(), e.getWorld(), e.getHand(), e.getHitVec());
        });
    }
}
