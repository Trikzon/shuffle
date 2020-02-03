/* ===========================================================================
 * Copyright 2020 Trikzon
 *
 * Shuffle is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * File: Config.java
 * Date: 2020-02-01
 * Revision:
 * Author: Trikzon
 * =========================================================================== */
package io.github.trikzon.shuffle;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.*;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

public class Shuffle implements ClientModInitializer
{
    private static final String MOD_ID = "shuffle";

    private static FabricKeyBinding keyBinding;

    private static boolean shuffleMode = false;

    private static int pressedDelay = 0;

    private static int slotToSwitchTo = -1;

    @Override
    public void onInitializeClient()
    {
        keyBinding = FabricKeyBinding.Builder.create(
                new Identifier(MOD_ID, "shuffle"),
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "Shuffle"
        ).build();

        KeyBindingRegistry.INSTANCE.addCategory("Shuffle");
        KeyBindingRegistry.INSTANCE.register(keyBinding);

        ClientTickCallback.EVENT.register(minecraftClient ->
        {
            if (minecraftClient.player == null) return;
            ClientPlayerEntity player = minecraftClient.player;

            if (keyBinding.isPressed() && pressedDelay == 0)
            {
                shuffleMode = !shuffleMode;
                if (shuffleMode)
                {
                    player.addChatMessage(new LiteralText("Shuffle Mode: Enabled").formatted(Formatting.GREEN), true);
                }
                else
                {
                    player.addChatMessage(new LiteralText("Shuffle Mode: Disabled").formatted(Formatting.RED), true);
                }
                pressedDelay = 10;
            }

            if (pressedDelay > 0)
            {
                --pressedDelay;
            }

            if (slotToSwitchTo >= 0 && slotToSwitchTo <= 8)
            {
                player.inventory.selectedSlot = slotToSwitchTo;
                slotToSwitchTo = -1;
            }
        });

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) ->
        {
            if (!world.isClient) return ActionResult.PASS;
            if (!shuffleMode) return ActionResult.PASS;
            if (player.isSpectator()) return ActionResult.PASS;

            Item itemInHand = player.getStackInHand(hand).getItem();
            if (Block.getBlockFromItem(itemInHand) != Blocks.AIR && itemInHand != Items.AIR)
            {
                ArrayList<Integer> slotsWithBlocks = new ArrayList<>();
                for (int i = 0; i <= 8; i++)
                {
                    Item item = player.inventory.main.get(i).getItem();
                    if (Block.getBlockFromItem(item) != Blocks.AIR && item != Items.AIR)
                    {
                        slotsWithBlocks.add(i);
                    }
                }

                int randomSlot = world.random.nextInt(slotsWithBlocks.size());

                slotToSwitchTo = slotsWithBlocks.get(randomSlot);
            }
            return ActionResult.PASS;
        });
    }
}
