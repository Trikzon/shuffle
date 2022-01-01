package com.trikzon.shuffle.client;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.ArrayList;

public class ShuffleClient {
    public static AbstractClientPlatform platform;

    private static boolean shuffleMode = false;
    private static boolean keyWasDown = false;
    private static int slotToSwitchTo = -1;

    public ShuffleClient(AbstractClientPlatform platform) {
        ShuffleClient.platform = platform;
    }

    public void onClientTick(MinecraftClient client) {
        ClientPlayerEntity player = client.player;
        if (player == null) return;

        if (platform.isShuffleKeyPressed() && !ShuffleClient.keyWasDown) {
            ShuffleClient.keyWasDown = true;

            ShuffleClient.shuffleMode = !ShuffleClient.shuffleMode;
            if (ShuffleClient.shuffleMode) {
                player.sendMessage(new TranslatableText("message.shuffle.enable"), true);
                player.playSound(SoundEvents.BLOCK_TRIPWIRE_CLICK_OFF, 0.5f, 1.0f);
            } else {
                player.sendMessage(new TranslatableText("message.shuffle.disable"), true);
                player.playSound(SoundEvents.BLOCK_TRIPWIRE_CLICK_ON, 0.5f, 1.0f);
            }
        } else if (!platform.isShuffleKeyPressed() && ShuffleClient.keyWasDown) {
            ShuffleClient.keyWasDown = false;
        }

        if (ShuffleClient.slotToSwitchTo >= 0 && ShuffleClient.slotToSwitchTo <= 8) {
            player.getInventory().selectedSlot = ShuffleClient.slotToSwitchTo;
            ShuffleClient.slotToSwitchTo = -1;
        }
    }

    public ActionResult onRightClickBlock(PlayerEntity player, World world, Hand hand) {
        if (!(!world.isClient || !ShuffleClient.shuffleMode || player.isSpectator())) {
            Item itemInHand = player.getStackInHand(hand).getItem();
            if (Block.getBlockFromItem(itemInHand) != Blocks.AIR && itemInHand != Items.AIR) {
                ArrayList<Integer> slotsWithBlocks = new ArrayList<>();
                for (int i = 0; i <= 8; i++) {
                    Item item = player.getInventory().main.get(i).getItem();
                    if (Block.getBlockFromItem(item) != Blocks.AIR && item != Items.AIR) {
                        slotsWithBlocks.add(i);
                    }
                }
                if (slotsWithBlocks.size() > 0) {
                    int randomSlot = world.random.nextInt(slotsWithBlocks.size());
                    ShuffleClient.slotToSwitchTo = slotsWithBlocks.get(randomSlot);
                }
            }
        }
        return ActionResult.PASS;
    }
}
