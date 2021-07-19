package com.trikzon.shuffle.forge;

import com.trikzon.shuffle.client.ShuffleClient;
import com.trikzon.shuffle.forge.client.ShuffleForgeClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(ShuffleClient.MOD_ID)
public class ShuffleForge {
    public ShuffleForge() {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ShuffleForgeClient::new);
    }
}
