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
 * File: Shuffle.java
 * Date: 2020-02-05
 * Revision:
 * Author: Trikzon
 * =========================================================================== */
package io.github.trikzon.shuffle.forge;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

@Mod(Shuffle.MOD_ID)
public class Shuffle
{
    public static final String MOD_ID = "shuffle";

    private static KeyBinding keyBinding;

    private static boolean shuffleMode = false;

    private static int pressedDelay = 0;
    // -1 when slot shouldn't be switched
    private static int slotToSwitchTo = -1;

    public Shuffle()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerTick);
        MinecraftForge.EVENT_BUS.addListener(this::onBlockRightClicked);
    }

    private void onClientSetup(final FMLClientSetupEvent event)
    {
        keyBinding = new KeyBinding(
                "key." + MOD_ID + ".shuffle",
                InputMappings.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "Shuffle"
        );
        ClientRegistry.registerKeyBinding(keyBinding);
    }

    private void onPlayerTick(final TickEvent.PlayerTickEvent event)
    {
        if (!event.side.isClient()) return;
        if (event.player == null) return;
        PlayerEntity player = event.player;

        if (keyBinding.isPressed() && pressedDelay == 0)
        {
            shuffleMode = !shuffleMode;
            if (shuffleMode)
            {
                player.sendStatusMessage(new TranslationTextComponent("message.shuffle.enable"), true);
            }
            else
            {
                player.sendStatusMessage(new TranslationTextComponent("message.shuffle.disable"), true);
            }
            pressedDelay = 10;
        }

        if (pressedDelay > 0)
        {
            --pressedDelay;
        }

        if (slotToSwitchTo >= 0 && slotToSwitchTo <= 8)
        {
            player.inventory.currentItem = slotToSwitchTo;
            slotToSwitchTo = -1;
        }
    }

    private void onBlockRightClicked(final PlayerInteractEvent.RightClickBlock event)
    {
        if (!event.getWorld().isRemote()) return;
        if (!shuffleMode) return;
        if (!(event.getEntity() instanceof PlayerEntity)) return;

        PlayerEntity player = (PlayerEntity) event.getEntity();

        Item itemInHand = player.getHeldItem(event.getHand()).getItem();
        if (Block.getBlockFromItem(itemInHand) != Blocks.AIR && itemInHand != Items.AIR)
        {
            ArrayList<Integer> slotsWithBlocks = new ArrayList<>();
            for (int i = 0; i <= 8; i++)
            {
                Item item = player.inventory.mainInventory.get(i).getItem();
                if (Block.getBlockFromItem(item) != Blocks.AIR && item != Items.AIR)
                {
                    slotsWithBlocks.add(i);
                }
            }
            if (slotsWithBlocks.size() > 0)
            {
                int randomSlot = event.getWorld().getRandom().nextInt(slotsWithBlocks.size());
                slotToSwitchTo = slotsWithBlocks.get(randomSlot);
            }
        }
    }
}