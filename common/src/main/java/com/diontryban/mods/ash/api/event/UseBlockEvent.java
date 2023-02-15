package com.diontryban.mods.ash.api.event;

import com.diontryban.mods.ash.ImplementationLoader;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public abstract class UseBlockEvent {
    private static final UseBlockEvent IMPL = ImplementationLoader.load(UseBlockEvent.class);

    public static void register(UseBlockCallback callback) {
        IMPL.registerImpl(callback);
    }

    @FunctionalInterface
    public interface UseBlockCallback {
        InteractionResult useBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult);
    }

    protected abstract void registerImpl(UseBlockCallback callback);
}
