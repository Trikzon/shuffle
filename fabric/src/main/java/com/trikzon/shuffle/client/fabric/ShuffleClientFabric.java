package com.trikzon.shuffle.client.fabric;

import com.trikzon.shuffle.Shuffle;
import com.trikzon.shuffle.client.ShuffleClient;
import net.fabricmc.api.ClientModInitializer;

public class ShuffleClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Shuffle.initialize();
        ShuffleClient.initialize();
    }
}
