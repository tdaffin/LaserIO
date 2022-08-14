package com.direwolf20.laserio.client.screens.widgets;

import com.direwolf20.laserio.common.LaserIO;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public class TextButton extends Button {
    private String text;

    public TextButton(int x, int y, int width, int height, String text, OnPress onPress) {
        super(x, y, width, height, TextComponent.EMPTY, onPress);
        this.text = text;
    }

    public TextButton(int x, int y, int width, int height, String text, OnPress onPress, OnTooltip onTooltip) {
        super(x, y, width, height, TextComponent.EMPTY, onPress, onTooltip);
        this.text = text;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        fill(stack, this.x, this.y, this.x + this.width, this.y + this.height, 0xFF353535);
        fill(stack, this.x + 1, this.y + 1, this.x + this.width - 1, this.y + this.height - 1, 0xFFD8D8D8);
        Font font = Minecraft.getInstance().font;
        stack.pushPose();
        float scale = 1f;
        stack.scale(scale, scale, scale);
        String msg = text;
        ;
        float x = (this.x + this.width / 2f) / scale - font.width(msg) / 2f;
        float y = (this.y + (this.height - font.lineHeight) / 2f / scale) / scale + 1;
        font.draw(stack, msg, x, y, Color.DARK_GRAY.getRGB());
        //drawCenteredString(stack, font, String.valueOf(value), (this.x + this.width / 2)*2, (this.y + (this.height - 8) / 2)*2, Color.DARK_GRAY.getRGB());
        stack.popPose();
        //drawCenteredString(stack, font, String.valueOf(channel), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | Mth.ceil(this.alpha * 255.0F) << 24);
    }

    @Override
    public void renderToolTip(PoseStack stack, int x, int y) {
        super.renderToolTip(stack, x, y);
    }

    @Override
    public void onClick(double p_onClick_1_, double p_onClick_3_) {
        super.onClick(p_onClick_1_, p_onClick_3_);
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        return super.mouseClicked(x, y, button);
    }

    @Override
    public void updateNarration(NarrationElementOutput p_169152_) {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
