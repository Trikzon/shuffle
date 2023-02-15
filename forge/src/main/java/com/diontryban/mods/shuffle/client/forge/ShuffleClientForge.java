package com.diontryban.mods.shuffle.client.forge;

import com.diontryban.mods.ash.api.forge.ModEventBus;
import com.diontryban.mods.shuffle.Shuffle;
import com.diontryban.mods.shuffle.client.ShuffleClient;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ShuffleClientForge {
    public static void init() {
        ModEventBus.register(Shuffle.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        ShuffleClient.init();
    }
}
