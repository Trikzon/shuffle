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

public class ShuffleClient {
    public static final String MOD_ID = "shuffle";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static AbstractPlatform platform;

    private static boolean shuffleMode = false;
    private static boolean keyWasDown = false;
    private static int slotToSwitchTo = -1;

    public ShuffleClient(AbstractPlatform platform) {
        ShuffleClient.platform = platform;
    }

    public void onClientTick(Minecraft client) {
        LocalPlayer player = client.player;
        if (player == null) return;

        if (ShuffleClient.platform.isShuffleKeyPressed() && !ShuffleClient.keyWasDown) {
            ShuffleClient.keyWasDown = true;

            ShuffleClient.shuffleMode = !ShuffleClient.shuffleMode;
            if (ShuffleClient.shuffleMode) {
                player.displayClientMessage(new TranslatableComponent("message.shuffle.enable"), true);
                player.playSound(SoundEvents.TRIPWIRE_CLICK_OFF, 0.5f, 1.0f);
            } else {
                player.displayClientMessage(new TranslatableComponent("message.shuffle.disable"), true);
                player.playSound(SoundEvents.TRIPWIRE_CLICK_ON, 0.5f, 0.75f);
            }
        } else if (!ShuffleClient.platform.isShuffleKeyPressed() && ShuffleClient.keyWasDown) {
            ShuffleClient.keyWasDown = false;
        }

        if (ShuffleClient.slotToSwitchTo >= 0 && ShuffleClient.slotToSwitchTo <= 8) {
            player.inventory.selected = ShuffleClient.slotToSwitchTo;
            ShuffleClient.slotToSwitchTo = -1;
        }
    }

    public InteractionResult onRightClickBlock(Player player, Level level, InteractionHand hand) {
        if (!(!level.isClientSide || !ShuffleClient.shuffleMode || player.isSpectator())) {
            Item itemInHand = player.getItemInHand(hand).getItem();
            if (Block.byItem(itemInHand) != Blocks.AIR && itemInHand != Items.AIR) {
                ArrayList<Integer> slotsWithBlocks = new ArrayList<>();
                for (int i = 0; i <= 8; i++) {
                    Item item = player.inventory.items.get(i).getItem();
                    if (Block.byItem(item) != Blocks.AIR && item != Items.AIR) {
                        slotsWithBlocks.add(i);
                    }
                }
                if (slotsWithBlocks.size() > 0) {
                    int randomSlot = level.random.nextInt(slotsWithBlocks.size());
                    ShuffleClient.slotToSwitchTo = slotsWithBlocks.get(randomSlot);
                }
            }
        }
        return InteractionResult.PASS;
    }
}
