package com.diontryban.shuffle.client.gui.screens;

import com.diontryban.ash.api.client.gui.screens.ModOptionsScreen;
import com.diontryban.ash.api.options.ModOptionsManager;
import com.diontryban.shuffle.Shuffle;
import com.diontryban.shuffle.options.ShuffleOptions;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ShuffleOptionsScreen extends ModOptionsScreen<ShuffleOptions> {

    public ShuffleOptionsScreen(ModOptionsManager<ShuffleOptions> options, Screen parent) {
        super(Component.literal(Shuffle.MOD_NAME), options, parent);
    }

    @Override
    protected void addOptions() {
        this.list.addBig(OptionInstance.createBoolean(
                "shuffle.options.use_weighted_random",
                value -> Tooltip.create(Component.translatable("shuffle.options.use_weighted_random.tooltip")),
                options.get().useWeightedRandom,
                value -> options.get().useWeightedRandom = value
        ));

    }
}
