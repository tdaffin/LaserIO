package com.direwolf20.laserio.client.screens.widgets;

import com.direwolf20.laserio.client.blockentityrenders.LaserNodeBERender;
import com.direwolf20.laserio.common.LaserIO;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public class ChannelButton extends Button {
    private final ResourceLocation resourceLocation = new ResourceLocation(LaserIO.MODID, "textures/gui/buttons/blankbutton.png");
    private int channel;
    private final Color colors[] = LaserNodeBERender.colors;
    private final Color darkText = Color.BLACK;
    private final Color lightText = Color.WHITE;
    private final Color textColors[] = {
        darkText,
        darkText,
        lightText,
        darkText,
        darkText,
        darkText,
        darkText,
        lightText,
        darkText,
        darkText,
        lightText,
        lightText,
        lightText,
        lightText,
        lightText,
        lightText,
    };

    public ChannelButton(int widthIn, int heightIn, int width, int height, int channel, OnPress onPress) {
        super(widthIn, heightIn, width, height, Component.empty(), onPress);
        this.channel = channel;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        //fill(stack, this.x, this.y, this.x + this.width, this.y + this.height, 0xFFa8a8a8);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, resourceLocation);
        blit(stack, this.x, this.y, 0, 0, width, height, width, height);
        fill(stack, this.x + 4, this.y + 4, this.x + this.width - 4, this.y + this.height - 4, colors[channel].getRGB());

        Font font = Minecraft.getInstance().font;
        stack.pushPose();
        float scale = 0.5f;
        stack.scale(scale, scale, scale);
        String msg = String.format("%,d", channel);
        float x = (this.x + this.width / 2f) / scale - font.width(msg) / 2f;
        float y = (this.y + (this.height - font.lineHeight) / 2f / scale) / scale + 1;
        font.draw(stack, msg, x, y, textColors[channel].getRGB());
        //drawCenteredString(stack, font, String.valueOf(value), (this.x + this.width / 2)*2, (this.y + (this.height - 8) / 2)*2, Color.DARK_GRAY.getRGB());
        stack.popPose();
        //drawCenteredString(stack, font, String.valueOf(channel), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | Mth.ceil(this.alpha * 255.0F) << 24);
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    @Override
    public void renderToolTip(PoseStack stack, int x, int y) {
        super.renderToolTip(stack, x, y);
    }
}
