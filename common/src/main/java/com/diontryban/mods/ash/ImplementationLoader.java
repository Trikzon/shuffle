package com.diontryban.mods.ash;

import com.diontryban.mods.shuffle.Shuffle;

import java.util.ServiceLoader;

public class ImplementationLoader {
    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException(
                        "Failed to load service for " + clazz.getName()
                ));
        Shuffle.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
