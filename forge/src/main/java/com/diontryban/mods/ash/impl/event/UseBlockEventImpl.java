package com.diontryban.mods.ash.impl.event;

import com.diontryban.mods.ash.api.event.UseBlockEvent;
import net.minecraft.world.InteractionResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class UseBlockEventImpl extends UseBlockEvent {
    @Override
    protected void registerImpl(UseBlockCallback callback) {
        MinecraftForge.EVENT_BUS.<PlayerInteractEvent.RightClickBlock>addListener(event -> {
            if (!event.getEntity().isSpectator()) {
                InteractionResult result = callback.useBlock(
                        event.getEntity(),
                        event.getLevel(),
                        event.getHand(),
                        event.getHitVec()
                );

                if (result.consumesAction()) {
                    event.setCancellationResult(result);
                    event.setCanceled(true);
                }
            }
        });
    }
}
