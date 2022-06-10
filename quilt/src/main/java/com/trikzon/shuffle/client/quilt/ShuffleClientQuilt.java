package com.trikzon.shuffle.client.quilt;

import com.trikzon.shuffle.Shuffle;
import com.trikzon.shuffle.client.ShuffleClient;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

public class ShuffleClientQuilt implements ClientModInitializer {
    @Override
    public void onInitializeClient(ModContainer mod) {
        Shuffle.initialize();
        ShuffleClient.initialize();
    }
}
