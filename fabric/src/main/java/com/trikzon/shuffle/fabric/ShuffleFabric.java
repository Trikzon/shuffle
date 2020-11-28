package com.trikzon.shuffle.fabric;

import com.mojang.blaze3d.platform.InputConstants;
import com.trikzon.shuffle.ShuffleCore;
import com.trikzon.shuffle.platform.AbstractPlatform;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.lwjgl.glfw.GLFW;

public class ShuffleFabric implements ClientModInitializer, AbstractPlatform {
    private static final KeyMapping keyBinding = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key." + ShuffleCore.MOD_ID + ".shuffle",
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R,
            "key.category." + ShuffleCore.MOD_ID
    ));

    public static ShuffleCore core;

    @Override
    public void onInitializeClient() {
        core = new ShuffleCore(this);

        ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);
        UseBlockCallback.EVENT.register(this::onRightClickBlock);
    }

    private void onClientTick(Minecraft client) {
        core.onClientTick(client);
    }

    private InteractionResult onRightClickBlock(Player player, Level level, InteractionHand hand, BlockHitResult result) {
        return this.core.onRightClickBlock(player, level, hand);
    }

    @Override
    public boolean isShuffleKeyPressed() {
        return keyBinding.isDown();
    }
}
