package com.trikzon.shuffle.client;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.Consumer;

public class PlatformClient {
    @ExpectPlatform
    public static KeyMapping registerKeyMapping(ResourceLocation resLoc, int key, String category) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void registerClientTickEvent(Consumer<Minecraft> callback) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void registerRightClickBlockEvent(RightClickBlockCallback callback) {
        throw new AssertionError();
    }

    @FunctionalInterface
    public interface RightClickBlockCallback {
        InteractionResult rightClickBlock(Player player, Level level, InteractionHand hand, BlockHitResult result);
    }
}
