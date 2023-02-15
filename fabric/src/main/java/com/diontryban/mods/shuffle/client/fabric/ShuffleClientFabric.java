package com.diontryban.mods.shuffle.client.fabric;

import com.diontryban.mods.shuffle.client.ShuffleClient;
import net.fabricmc.api.ClientModInitializer;

public class ShuffleClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ShuffleClient.init();
    }
}
