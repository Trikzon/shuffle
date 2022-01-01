package com.trikzon.shuffle.forge;

import com.trikzon.shuffle.Shuffle;
import com.trikzon.shuffle.client.forge.ShuffleClientForge;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(Shuffle.MOD_ID)
public class ShuffleForge {
    public ShuffleForge() {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ShuffleClientForge::new);
    }
}
