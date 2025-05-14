package com.diontryban.shuffle.client.gui.widgets;

import com.diontryban.ash_api.options.ModOptionsManager;
import com.diontryban.shuffle.options.ShuffleOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class HotbarLockButtons extends AbstractWidget {
    private final ModOptionsManager<ShuffleOptions> options;
    private final int offset;
    private final boolean[] values;
    private static final WidgetSprites SPRITES = new WidgetSprites(
            ResourceLocation.withDefaultNamespace("widget/unlocked_button"),
            ResourceLocation.withDefaultNamespace("widget/locked_button"),
            ResourceLocation.withDefaultNamespace("widget/unlocked_button_highlighted"),
            ResourceLocation.withDefaultNamespace("widget/locked_button_highlighted")
    );

    public HotbarLockButtons(int offset, Component message, ModOptionsManager<ShuffleOptions> options) {
        super(offset, 0, 20*9+offset, 20, message);
        this.options = options;
        this.offset = offset;
        this.values = options.get().slotStates;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int i1, float v) {
        for (int j = 0; j < 9; j++) {
            ResourceLocation sprite = SPRITES.get(values[j], false);
            guiGraphics.blitSprite(sprite, offset+this.getX()+20*j, this.getY(), 20, 20);
        }
        guiGraphics.drawCenteredString(
                Minecraft.getInstance().font,
                Component.translatable("shuffle.options.slots"),
                offset+this.getX()+85,
                this.getY() - 20,
                0xFFFFFF);
    }

    @Override
    public void onClick(double x, double y) {
        int slotClicked = (int) ((x - getX() - offset) / 20);
        if (slotClicked < 0 || slotClicked > 8)
            return;
        this.values[slotClicked] = !this.values[slotClicked];
        options.get().slotStates[slotClicked] = this.values[slotClicked];
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }
}
