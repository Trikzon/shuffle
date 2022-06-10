package com.trikzon.shuffle.client.forge;

import com.trikzon.shuffle.Shuffle;
import com.trikzon.shuffle.client.ShuffleClient;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ShuffleClientForge {
    public ShuffleClientForge() {
        Shuffle.initialize();
        ShuffleClient.initialize();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(PlatformClientImpl::onClientSetup);
    }
}
