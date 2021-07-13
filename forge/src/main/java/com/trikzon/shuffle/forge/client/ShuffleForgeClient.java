package com.trikzon.shuffle.forge.client;

import com.trikzon.shuffle.ShuffleCore;
import com.trikzon.shuffle.platform.AbstractPlatform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.glfw.GLFW;

public class ShuffleForgeClient implements AbstractPlatform {
    private static final KeyBinding keyBinding = new KeyBinding(
            "key." + ShuffleCore.MOD_ID + ".shuffle",
            GLFW.GLFW_KEY_R,
            "key.category." + ShuffleCore.MOD_ID
    );

    private ShuffleCore core;

    public ShuffleForgeClient() {
        this.core = new ShuffleCore(this);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
        MinecraftForge.EVENT_BUS.addListener(this::onClientTick);
        MinecraftForge.EVENT_BUS.addListener(this::onRightClickBlock);
    }

    private void onClientSetup(final FMLClientSetupEvent event) {
        ClientRegistry.registerKeyBinding(keyBinding);
    }

    private void onClientTick(final TickEvent.ClientTickEvent event) {
        this.core.onClientTick(Minecraft.getInstance());
    }

    private void onRightClickBlock(final PlayerInteractEvent.RightClickBlock event) {
        this.core.onRightClickBlock(event.getPlayer(), event.getWorld(), event.getHand());
    }

    @Override
    public boolean isShuffleKeyPressed() {
        return keyBinding.isDown();
    }
}
