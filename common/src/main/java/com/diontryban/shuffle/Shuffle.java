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

package com.diontryban.shuffle;

import com.diontryban.ash_api.options.ModOptionsManager;
import com.diontryban.shuffle.options.ShuffleOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Shuffle {
    public static final String MOD_ID = "shuffle";
    public static final String MOD_NAME = "Shuffle";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);
    public static final ModOptionsManager<ShuffleOptions> OPTIONS = new ModOptionsManager<>(MOD_ID, ShuffleOptions.class);
}
