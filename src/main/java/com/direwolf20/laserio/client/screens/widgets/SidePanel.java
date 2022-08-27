package com.direwolf20.laserio.client.screens.widgets;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.awt.Color;

public class SidePanel extends Panel {

    private Color color;

    public SidePanel(int pX, int pY, int pWidth, int pHeight, Component pMessage, Color color) {
        super(pX, pY, pWidth, pHeight, pMessage);
        this.color = color;
        setColorsFromHighlight(color);
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        
        //fill(pPoseStack, this.x + 4, this.y + 4, this.x + this.width - 4, this.y + 8, color);

        var font = Minecraft.getInstance().font;
        var text = getMessage().getString();
        drawString(pPoseStack, font, text, x + 4, y + 4, 0xffffff);
        //drawString(pPoseStack, font, genText, 10, 45, 0xffffff);
    }
    
}
