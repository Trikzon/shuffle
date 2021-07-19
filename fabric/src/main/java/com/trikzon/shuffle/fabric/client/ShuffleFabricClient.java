package com.trikzon.shuffle.fabric.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.trikzon.shuffle.client.ShuffleClient;
import com.trikzon.shuffle.client.platform.AbstractPlatform;
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

public class ShuffleFabricClient implements ClientModInitializer, AbstractPlatform {
    private static final KeyMapping keyBinding = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key." + ShuffleClient.MOD_ID + ".shuffle",
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R,
            "key.category." + ShuffleClient.MOD_ID
    ));

    public static ShuffleClient core;

    @Override
    public void onInitializeClient() {
        ShuffleFabricClient.core = new ShuffleClient(this);

        ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);
        UseBlockCallback.EVENT.register(this::onRightClickBlock);
    }

    private void onClientTick(Minecraft client) {
        ShuffleFabricClient.core.onClientTick(client);
    }

    private InteractionResult onRightClickBlock(Player player, Level level, InteractionHand hand, BlockHitResult result) {
        return ShuffleFabricClient.core.onRightClickBlock(player, level, hand);
    }

    @Override
    public boolean isShuffleKeyPressed() {
        return ShuffleFabricClient.keyBinding.isDown();
    }
}
