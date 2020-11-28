package com.trikzon.shuffle.fabric;

import com.trikzon.shuffle.ShuffleCore;
import com.trikzon.shuffle.platform.AbstractPlatform;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;

public class ShuffleFabric implements ModInitializer, AbstractPlatform {
    @Override
    public void onInitialize() {
        ShuffleCore.init(this);
    }
}
