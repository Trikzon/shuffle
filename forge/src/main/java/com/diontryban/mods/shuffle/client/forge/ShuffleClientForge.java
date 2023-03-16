/*
 * This file is part of Shuffle.
 * A copy of this program can be found at https://github.com/Trikzon/shuffle.
 * Copyright (C) 2023 Dion Tryban
 *
 * Shuffle is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * Shuffle is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Shuffle. If not, see <https://www.gnu.org/licenses/>.
 */

package com.diontryban.mods.shuffle.client.forge;

import com.diontryban.mods.ash.api.modloader.forge.ModEventBus;
import com.diontryban.mods.shuffle.Shuffle;
import com.diontryban.mods.shuffle.client.ShuffleClient;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ShuffleClientForge {
    public static void init() {
        ModEventBus.register(Shuffle.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        ShuffleClient.init();
    }
}
