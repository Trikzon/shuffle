package com.trikzon.shuffle.forge.client;

import com.trikzon.shuffle.client.ShuffleClient;
import com.trikzon.shuffle.client.platform.AbstractPlatform;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.glfw.GLFW;

public class ShuffleForgeClient implements AbstractPlatform {
    private static final KeyMapping keyMapping = new KeyMapping(
            "key." + ShuffleClient.MOD_ID + ".shuffle",
            GLFW.GLFW_KEY_R,
            "key.category." + ShuffleClient.MOD_ID
    );

    private final ShuffleClient core;

    public ShuffleForgeClient() {
        this.core = new ShuffleClient(this);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
        MinecraftForge.EVENT_BUS.addListener(this::onClientTick);
        MinecraftForge.EVENT_BUS.addListener(this::onRightClickBlock);
    }

    private void onClientSetup(final FMLClientSetupEvent event) {
        ClientRegistry.registerKeyBinding(keyMapping);
    }

    private void onClientTick(final TickEvent.ClientTickEvent event) {
        this.core.onClientTick(Minecraft.getInstance());
    }

    private void onRightClickBlock(final PlayerInteractEvent.RightClickBlock event) {
        this.core.onRightClickBlock(event.getPlayer(), event.getWorld(), event.getHand());
    }

    @Override
    public boolean isShuffleKeyPressed() {
        return keyMapping.isDown();
    }
}
