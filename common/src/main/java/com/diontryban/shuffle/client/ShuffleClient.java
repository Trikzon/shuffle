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

package com.diontryban.shuffle.client;

import com.diontryban.ash_api.client.event.ClientTickEvents;
import com.diontryban.ash_api.client.gui.screens.ModOptionsScreenRegistry;
import com.diontryban.ash_api.client.input.KeyMappingRegistry;
import com.diontryban.ash_api.event.UseBlockEvent;
import com.diontryban.ash_api.modloader.CommonClientModInitializer;
import com.diontryban.shuffle.Shuffle;
import com.diontryban.shuffle.client.gui.screens.ShuffleOptionsScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.ToIntBiFunction;

public class ShuffleClient extends CommonClientModInitializer {
    private static final KeyMapping KEY = KeyMappingRegistry.registerKeyMapping(
            new ResourceLocation(Shuffle.MOD_ID, "shuffle"),
            GLFW.GLFW_KEY_R,
            Shuffle.MOD_ID
    );

    private static boolean shuffle = false;
    private static boolean keyWasDown = false;
    private static int slotToSwitchTo = -1;

    @Override
    public void onInitializeClient() {
        ModOptionsScreenRegistry.registerModOptionsScreen(Shuffle.OPTIONS, ShuffleOptionsScreen::new);
        ClientTickEvents.registerStart(ShuffleClient::onClientStartTick);
        UseBlockEvent.register(ShuffleClient::onRightClickBlock);
    }

    private static void onClientStartTick(Minecraft client) {
        final var player = client.player;
        if (player == null) { return; }

        if (KEY.isDown() && !keyWasDown) {
            keyWasDown = true;

            shuffle = !shuffle;
            if (shuffle) {
                player.displayClientMessage(Component.translatable("message.shuffle.enable"), true);
                player.playSound(SoundEvents.TRIPWIRE_CLICK_OFF, 0.5f, 1.0f);
            } else {
                player.displayClientMessage(Component.translatable("message.shuffle.disable"), true);
                player.playSound(SoundEvents.TRIPWIRE_CLICK_ON, 0.5f, 1.0f);
            }
        } else if (!KEY.isDown() && keyWasDown) {
            keyWasDown = false;
        }

        if (slotToSwitchTo >= 0 && slotToSwitchTo <= 8) {
            player.getInventory().selected = slotToSwitchTo;
            slotToSwitchTo = -1;
        }
    }

    private static InteractionResult onRightClickBlock(
            Player player,
            Level level,
            InteractionHand hand,
            BlockHitResult hitResult
    ) {
        if (shuffle && level.isClientSide && !player.isSpectator()) {
            final var itemInHand = player.getItemInHand(hand).getItem();
            // Only shuffle if the held item is a block, therefore it's being placed.
            if (Block.byItem(itemInHand) != Blocks.AIR) {
                final var items = player.getInventory().items;

                // Check whether to use weighted or random logic
                if (Shuffle.OPTIONS.get().useWeightedRandom) {
                    slotToSwitchTo = switchSlotWeighted(items, level.random);
                } else {
                    slotToSwitchTo = switchSlotRandom(items, level.random);
                }
            }
        }
        return InteractionResult.PASS;
    }

    /**
     * Switch to a given slot on the hotbar randomly.
     *
     * @param items the list of items representing the player's hotbar; checks the first 9 slots
     * @param random a random instance
     * @return the index of the slot to switch to, or {@code -1} if not to switch
     */
    private static int switchSlotRandom(NonNullList<ItemStack> items, RandomSource random) {
        return switchSlotLogic(items, random,
                (i, stack) -> i,
                (list, rand) -> list.get(rand.nextInt(list.size()))
        );
    }

    /**
     * Switch to a given slot on the hotbar randomly using the item count as a weight.
     *
     * @param items the list of items representing the player's hotbar; checks the first 9 slots
     * @param random a random instance
     * @return the index of the slot to switch to, or {@code -1} if not to switch
     */
    private static int switchSlotWeighted(NonNullList<ItemStack> items, RandomSource random) {
        return switchSlotLogic(items, random,
                (i, stack) -> WeightedEntry.wrap(i, stack.getCount()),
                (list, rand) -> WeightedRandom.getRandomItem(rand, list)
                        .map(WeightedEntry.Wrapper::getData)
                        .orElse(-1)
        );
    }

    /**
     * Switch to a given slot on the hotbar.
     *
     * @param items the list of items representing the player's hotbar; checks the first 9 slots.
     * @param random a random instance
     * @param createEntry creates an entry to store in the list
     * @param pickRandom a function to pick a random element from a list
     * @return the index of the slot to switch to, or {@code -1} if not to switch
     * @param <T> the type of the data stored in the list
     */
    private static <T> int switchSlotLogic(
            NonNullList<ItemStack> items,
            RandomSource random,
            BiFunction<Integer, ItemStack, T> createEntry,
            ToIntBiFunction<List<T>, RandomSource> pickRandom
    ) {
        final List<T> slotsWithBlocks = new ArrayList<>();

        // Check hotbar and collect all items that can be placed.
        for (var i = 0; i <= 8; i++) {
            final var stack = items.get(i);
            // Make sure that the item can be placed as a block.
            if (Block.byItem(stack.getItem()) != Blocks.AIR) {
                slotsWithBlocks.add(createEntry.apply(i, stack));
            }
        }

        // Pick a random element if available, otherwise set to -1.
        return !slotsWithBlocks.isEmpty() ? pickRandom.applyAsInt(slotsWithBlocks, random) : -1;
    }
}
