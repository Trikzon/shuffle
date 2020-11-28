package com.trikzon.shuffle.forge;

import com.trikzon.shuffle.ShuffleCore;
import com.trikzon.shuffle.platform.AbstractPlatform;
import net.minecraftforge.fml.common.Mod;

@Mod(ShuffleCore.MOD_ID)
public class ShuffleForge implements AbstractPlatform {
    public ShuffleForge() {
        ShuffleCore.init(this);
    }
}
