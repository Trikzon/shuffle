package com.trikzon.shuffle.client;

import com.trikzon.shuffle.Shuffle;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

public class ShuffleClient {
    private static boolean shuffleMode = false;
    private static boolean keyWasDown = false;
    private static int slotToSwitchTo = -1;

    private static KeyMapping keyMapping;

    public static void initialize() {
        ShuffleClient.keyMapping = PlatformClient.registerKeyMapping(
                new ResourceLocation(Shuffle.MOD_ID, "shuffle"),
                GLFW.GLFW_KEY_R,
                "key.categories." + Shuffle.MOD_ID
        );
        PlatformClient.registerClientTickEvent(ShuffleClient::onClientTick);
        PlatformClient.registerRightClickBlockEvent(ShuffleClient::onRightClickBlock);
    }

    private static void onClientTick(Minecraft client) {
        LocalPlayer player = client.player;
        if (player == null) return;

        if (keyMapping.isDown() && !ShuffleClient.keyWasDown) {
            ShuffleClient.keyWasDown = true;

            ShuffleClient.shuffleMode = !ShuffleClient.shuffleMode;
            if (ShuffleClient.shuffleMode) {
                player.displayClientMessage(Component.translatable("message.shuffle.enable"), true);
                player.playSound(SoundEvents.TRIPWIRE_CLICK_OFF, 0.5f, 1.0f);
            } else {
                player.displayClientMessage(Component.translatable("message.shuffle.disable"), true);
                player.playSound(SoundEvents.TRIPWIRE_CLICK_ON, 0.5f, 1.0f);
            }
        } else if (!keyMapping.isDown() && ShuffleClient.keyWasDown) {
            ShuffleClient.keyWasDown = false;
        }

        if (ShuffleClient.slotToSwitchTo >= 0 && ShuffleClient.slotToSwitchTo <= 8) {
            player.getInventory().selected = ShuffleClient.slotToSwitchTo;
            ShuffleClient.slotToSwitchTo = -1;
        }
    }

    private static InteractionResult onRightClickBlock(
            Player player,
            Level level,
            InteractionHand hand,
            BlockHitResult result
    ) {
        if (ShuffleClient.shuffleMode && level.isClientSide && !player.isSpectator()) {
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
                    ShuffleClient.slotToSwitchTo = slotsWithBlocks.get(randomSlot);
                }
            }
        }
        return InteractionResult.PASS;
    }
}
