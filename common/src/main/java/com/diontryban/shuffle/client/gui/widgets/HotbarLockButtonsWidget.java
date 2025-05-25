package com.diontryban.shuffle.client.gui.widgets;

import com.diontryban.ash_api.options.ModOptionsManager;
import com.diontryban.shuffle.options.ShuffleOptions;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;

public class HotbarLockButtonsWidget extends AbstractWidget {
    private final ModOptionsManager<ShuffleOptions> options;
    private final int offset;

    private static final ResourceLocation HOTBAR_SPRITE = ResourceLocation.withDefaultNamespace("hud/hotbar");
    private static final ResourceLocation LOCKED_SPRITE = ResourceLocation.withDefaultNamespace("container/cartography_table/locked");

    public HotbarLockButtonsWidget(int offset, ModOptionsManager<ShuffleOptions> options) {
        super(offset, 1, 20*9, 20, Component.empty());
        this.options = options;
        this.offset = offset;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float v) {
        guiGraphics.drawCenteredString(
                Minecraft.getInstance().font,
                Component.translatable("shuffle.options.hotbar_lock"),
                offset + this.getX() + 85,
                this.getY() - 20,
                0xFFFFFF
        );

        guiGraphics.drawCenteredString(
                Minecraft.getInstance().font,
                Component.translatable("shuffle.options.hotbar_lock.description").withStyle(ChatFormatting.GRAY),
                offset + this.getX() + 85,
                this.getY() - 10,
                0xFFFFFF
        );

        guiGraphics.blitSprite(RenderType::guiTextured, HOTBAR_SPRITE, offset + this.getX(), this.getY(), 182, 22);

        for (int slot = 0; slot < 9; slot++) {
            final var slotX = getSlotX(slot);
            final var hovering = mouseX >= slotX && mouseX < slotX + 20 && mouseY >= this.getY() && mouseY < this.getY() + 20;

            if (this.options.get().lockedSlots[slot]) {
                guiGraphics.blitSprite(RenderType::guiTextured, LOCKED_SPRITE, slotX + 5, this.getY() + 5, 10, 14);
            } else if (hovering) {
                guiGraphics.blitSprite(RenderType::guiTextured, LOCKED_SPRITE, slotX + 5, this.getY() + 5, 10, 14, ARGB.color(ARGB.as8BitChannel(0.5f), -1));
            }

            if (hovering) {
                guiGraphics.renderTooltip(
                        Minecraft.getInstance().font,
                        this.options.get().lockedSlots[slot] ?
                            Component.translatable("shuffle.options.hotbar_lock.tooltip.on", slot + 1) :
                            Component.translatable("shuffle.options.hotbar_lock.tooltip.off", slot + 1),
                        mouseX,
                        mouseY
                );
            }
        }
    }

    @Override
    public void onClick(double x, double y) {
        int slot = (int) ((x - getX() - offset) / 20);
        if (slot < 0 || slot > 8) {
            return;
        }

        this.options.get().lockedSlots[slot] = !this.options.get().lockedSlots[slot];
    }

    @Override
    protected boolean clicked(double mouseX, double mouseY) {
        return this.active && this.visible && mouseX >= (double)this.getSlotX(0) && mouseY >= (double)this.getY() && mouseX < (double)(this.getSlotX(0) + this.getWidth()) && mouseY < (double)(this.getY() + this.getHeight());
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }

    private int getSlotX(int slot) {
        return offset + this.getX() + 1 + (20 * slot);
    }
}
