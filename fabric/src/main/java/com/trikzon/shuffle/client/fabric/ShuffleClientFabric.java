package com.trikzon.shuffle.client.fabric;

import com.trikzon.shuffle.Shuffle;
import com.trikzon.shuffle.client.AbstractClientPlatform;
import com.trikzon.shuffle.client.ShuffleClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;

public class ShuffleClientFabric implements ClientModInitializer, AbstractClientPlatform {
    private static final KeyBinding keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key." + Shuffle.MOD_ID + ".shuffle",
            InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R,
            "key.category." + Shuffle.MOD_ID
    ));

    public ShuffleClient mod;

    @Override
    public void onInitializeClient() {
        this.mod = new ShuffleClient(this);

        ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);
        UseBlockCallback.EVENT.register(this::onRightClickBlock);
    }

    private void onClientTick(MinecraftClient client) {
        this.mod.onClientTick(client);
    }

    private ActionResult onRightClickBlock(PlayerEntity player, World world, Hand hand, BlockHitResult result) {
        return this.mod.onRightClickBlock(player, world, hand);
    }

    @Override
    public boolean isShuffleKeyPressed() {
        return keyBinding.isPressed();
    }
}
