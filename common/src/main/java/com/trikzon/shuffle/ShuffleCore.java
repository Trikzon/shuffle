package com.trikzon.shuffle;

import com.trikzon.shuffle.platform.AbstractPlatform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShuffleCore {
    public static final String MOD_ID = "shuffle";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static AbstractPlatform platform;

    public static void init(AbstractPlatform platform) {
        ShuffleCore.platform = platform;
    }
}
