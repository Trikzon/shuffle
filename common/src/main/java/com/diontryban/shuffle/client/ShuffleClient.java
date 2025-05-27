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

import com.diontryban.ash_api.client.event.ClientTickEvent;
import com.diontryban.ash_api.client.gui.screens.ModOptionsScreenRegistry;
import com.diontryban.ash_api.client.input.KeyMappingRegistry;
import com.diontryban.ash_api.event.UseBlockEvent;
import com.diontryban.shuffle.Shuffle;
import com.diontryban.shuffle.client.gui.screens.ShuffleOptionsScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import org.lwjgl.glfw.GLFW;

import java.util.function.BiFunction;

public class ShuffleClient {
    private static KeyMapping keyMapping;

    private static boolean shuffle = false;
    private static boolean keyWasDown = false;
    private static int slotToSwitchTo = -1;

    public static void init() {
        keyMapping = KeyMappingRegistry.register(
                ResourceLocation.fromNamespaceAndPath(Shuffle.MOD_ID, "shuffle"),
                GLFW.GLFW_KEY_R,
                Shuffle.MOD_ID
        );

        ModOptionsScreenRegistry.register(Shuffle.OPTIONS, ShuffleOptionsScreen::new);
        ClientTickEvent.Pre.register(ShuffleClient::onClientTickPre);
        UseBlockEvent.register(ShuffleClient::onUseBlock);
    }

    private static void onClientTickPre(Minecraft client) {
        final var player = client.player;
        if (player == null) { return; }

        if (keyMapping.isDown() && !keyWasDown) {
            keyWasDown = true;

            shuffle = !shuffle;
            if (shuffle) {
                player.displayClientMessage(Component.translatable("message.shuffle.enable"), true);

                if (Shuffle.OPTIONS.get().playSoundEffects) {
                    player.playSound(SoundEvents.TRIPWIRE_CLICK_OFF, 0.5f, 1.0f);
                }
            } else {
                player.displayClientMessage(Component.translatable("message.shuffle.disable"), true);

                if (Shuffle.OPTIONS.get().playSoundEffects) {
                    player.playSound(SoundEvents.TRIPWIRE_CLICK_ON, 0.5f, 1.0f);
                }
            }
        } else if (!keyMapping.isDown() && keyWasDown) {
            keyWasDown = false;
        }

        if (slotToSwitchTo >= 0 && slotToSwitchTo <= 8) {
            player.getInventory().setSelectedSlot(slotToSwitchTo);
            slotToSwitchTo = -1;
        }
    }

    private static InteractionResult onUseBlock(
            Player player,
            Level level,
            InteractionHand hand,
            BlockHitResult hitResult
    ) {
        if (shuffle && level.isClientSide && !player.isSpectator()) {
            final var itemInHand = player.getItemInHand(hand).getItem();
            // Only shuffle if the held item is a block, therefore it's being placed.
            if (Block.byItem(itemInHand) != Blocks.AIR) {
                final var items = player.getInventory().getNonEquipmentItems();

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
     * Randomly choose a slot to switch to.
     *
     * @param items the list of items representing the player's hotbar; checks the first 9 slots
     * @param random a random instance
     * @return the index of the slot to switch to, or {@code -1} if not to switch
     */
    private static int switchSlotRandom(NonNullList<ItemStack> items, RandomSource random) {
        return switchSlotLogic(items, random, (slotIdx, stack) -> 1);
    }

    /**
     * Randomly choose a slot to switch to using the item's count as a weight.
     *
     * @param items the list of items representing the player's hotbar; checks the first 9 slots
     * @param random a random instance
     * @return the index of the slot to switch to, or {@code -1} if not to switch
     */
    private static int switchSlotWeighted(NonNullList<ItemStack> items, RandomSource random) {
        return switchSlotLogic(items, random, (slotIdx, stack) -> stack.getCount());
    }

    /**
     * Choose a slot to switch to using a weight per slot.
     *
     * @param items the list of items representing the player's hotbar; checks the first 9 slots.
     * @param random a random instance
     * @param calculateWeight a function to get the weight of a given slot
     * @return the index of the slot to switch to, or {@code -1} if not to switch
     */
    private static int switchSlotLogic(
            NonNullList<ItemStack> items,
            RandomSource random,
            BiFunction<Integer, ItemStack, Integer> calculateWeight
    ) {
        final var validSlotsBuilder = new WeightedList.Builder<Integer>();

        // Check hotbar and collect all items that can be placed.
        for (var slotIdx = 0; slotIdx < 9; slotIdx++) {
            final var stack = items.get(slotIdx);
            // Make sure that the item can be placed as a block.
            if (Block.byItem(stack.getItem()) != Blocks.AIR && !Shuffle.OPTIONS.get().lockedSlots[slotIdx]) {
                validSlotsBuilder.add(slotIdx, calculateWeight.apply(slotIdx, stack));
            }
        }

        final var validSlots = validSlotsBuilder.build();

        return validSlots.getRandom(random).orElse(-1);
    }
}
