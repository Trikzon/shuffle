package com.trikzon.shuffle.client.forge;

import com.trikzon.shuffle.Shuffle;
import com.trikzon.shuffle.client.AbstractClientPlatform;
import com.trikzon.shuffle.client.ShuffleClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmlclient.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

public class ShuffleClientForge implements AbstractClientPlatform {
    private static final KeyBinding keyBinding = new KeyBinding(
            "key." + Shuffle.MOD_ID + ".shuffle",
            GLFW.GLFW_KEY_R,
            "key.category." + Shuffle.MOD_ID
    );

    private final ShuffleClient mod;

    public ShuffleClientForge() {
        this.mod = new ShuffleClient(this);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
        MinecraftForge.EVENT_BUS.addListener(this::onClientTick);
        MinecraftForge.EVENT_BUS.addListener(this::onRightClickBlock);
    }

    private void onClientSetup(final FMLClientSetupEvent event) {
        ClientRegistry.registerKeyBinding(keyBinding);
    }

    private void onClientTick(final TickEvent.ClientTickEvent event) {
        this.mod.onClientTick(MinecraftClient.getInstance());
    }

    private void onRightClickBlock(final PlayerInteractEvent.RightClickBlock event) {
        this.mod.onRightClickBlock(event.getPlayer(), event.getWorld(), event.getHand());
    }

    @Override
    public boolean isShuffleKeyPressed() {
        return keyBinding.isPressed();
    }
}
