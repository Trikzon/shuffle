package com.trikzon.shuffle.forge;

import com.trikzon.shuffle.ShuffleCore;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(ShuffleCore.MOD_ID)
public class ShuffleForge {
    public ShuffleForge() {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ShuffleForgeClient::new);
    }
}
