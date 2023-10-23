package com.diontryban.shuffle.options;

import com.diontryban.ash.api.options.ModOptions;
import com.google.gson.annotations.SerializedName;

public class ShuffleOptions extends ModOptions {

    @SerializedName("use_weighted_random")
    public boolean useWeightedRandom = false;
}
