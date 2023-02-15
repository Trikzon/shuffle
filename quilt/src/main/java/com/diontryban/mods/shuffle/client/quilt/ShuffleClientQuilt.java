package com.diontryban.mods.shuffle.client.quilt;

import com.diontryban.mods.shuffle.client.ShuffleClient;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

public class ShuffleClientQuilt implements ClientModInitializer {
    @Override
    public void onInitializeClient(ModContainer mod) {
        ShuffleClient.init();
    }
}
