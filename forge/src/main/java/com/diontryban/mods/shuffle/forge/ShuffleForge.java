package com.diontryban.mods.shuffle.forge;

import com.diontryban.mods.shuffle.Shuffle;
import com.diontryban.mods.shuffle.client.forge.ShuffleClientForge;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(Shuffle.MOD_ID)
public class ShuffleForge {
    public ShuffleForge() {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ShuffleClientForge::init);
    }
}
