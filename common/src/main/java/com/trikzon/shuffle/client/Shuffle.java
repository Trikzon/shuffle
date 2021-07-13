package com.trikzon.shuffle.client;

import com.trikzon.shuffle.client.platform.AbstractPlatform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class Shuffle {
    public static final String MOD_ID = "shuffle";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static AbstractPlatform platform;

    private static boolean shuffleMode = false;
    private static boolean keyWasDown = false;
    private static int slotToSwitchTo = -1;

    public Shuffle(AbstractPlatform platform) {
        Shuffle.platform = platform;
    }

    public void onClientTick(Minecraft client) {
        LocalPlayer player = client.player;
        if (player == null) return;

        if (platform.isShuffleKeyPressed() && !Shuffle.keyWasDown) {
            keyWasDown = true;

            Shuffle.shuffleMode = !Shuffle.shuffleMode;
            if (Shuffle.shuffleMode) {
                player.displayClientMessage(new TranslatableComponent("message.shuffle.enable"), true);
                player.playSound(SoundEvents.TRIPWIRE_CLICK_OFF, 0.5f, 1.0f);
            } else {
                player.displayClientMessage(new TranslatableComponent("message.shuffle.disable"), true);
                player.playSound(SoundEvents.TRIPWIRE_CLICK_ON, 0.5f, 0.75f);
            }
        } else if (!platform.isShuffleKeyPressed() && Shuffle.keyWasDown) {
            Shuffle.keyWasDown = false;
        }

        if (Shuffle.slotToSwitchTo >= 0 && Shuffle.slotToSwitchTo <= 8) {
            player.getInventory().selected = Shuffle.slotToSwitchTo;
            Shuffle.slotToSwitchTo = -1;
        }
    }

    public InteractionResult onRightClickBlock(Player player, Level level, InteractionHand hand) {
        if (!(!level.isClientSide || !Shuffle.shuffleMode || player.isSpectator())) {
            Item itemInHand = player.getItemInHand(hand).getItem();
            if (Block.byItem(itemInHand) != Blocks.AIR && itemInHand != Items.AIR) {
                ArrayList<Integer> slotsWithBlocks = new ArrayList<>();
                for (int i = 0; i <= 8; i++) {
                    Item item = player.getInventory().items.get(i).getItem();
                    if (Block.byItem(item) != Blocks.AIR && item != Items.AIR) {
                        slotsWithBlocks.add(i);
                    }
                }
                if (slotsWithBlocks.size() > 0) {
                    int randomSlot = level.random.nextInt(slotsWithBlocks.size());
                    Shuffle.slotToSwitchTo = slotsWithBlocks.get(randomSlot);
                }
            }
        }
        return InteractionResult.PASS;
    }
}
