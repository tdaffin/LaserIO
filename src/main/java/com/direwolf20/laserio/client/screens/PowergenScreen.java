package com.direwolf20.laserio.client.screens;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.direwolf20.laserio.client.screens.widgets.Panel;
import com.direwolf20.laserio.client.screens.widgets.SlotPanel;
import com.direwolf20.laserio.common.containers.PowergenContainer;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.energy.IEnergyStorage;

public class PowergenScreen extends AbstractContainerScreen<PowergenContainer> {

    public final List<Widget> backgroundRenderables = new ArrayList<>();

    public PowergenScreen(PowergenContainer pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    public void init() {
        super.init();
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        backgroundRenderables.add(new Panel(relX, relY, imageWidth, imageHeight));
        for(var slot : menu.getSlots()){
            int x = relX + slot.x - 1;
            int y = relY + slot.y - 1;
            var slotPanel = new SlotPanel(x, y);
            backgroundRenderables.add(slotPanel);
        }
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
        String energyText = "Energy: ";
        var storage = menu.getEnergyStorage().resolve().get();
        if (storage != null){
            int energy = storage.getEnergyStored();
            int capacity = storage.getMaxEnergyStored();
            energyText += energy + " / " + capacity;
        } else
            energyText += "No Storage";
        var genTicks = menu.getGenTicks();
        String genText = "Idle";
        if (genTicks > 0){
            genText = "Generation: " + genTicks;
        }
        var font = Minecraft.getInstance().font;
        drawString(matrixStack, font, energyText, 10, 10, 0xffffff);
        drawString(matrixStack, font, genText, 10, 45, 0xffffff);
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        //RenderSystem.setShaderTexture(0, GUI);
        //int relX = (this.width - this.imageWidth) / 2;
        //int relY = (this.height - this.imageHeight) / 2;
        //fill(pPoseStack, relX, relY, relX + imageWidth, relY + imageHeight, Color.LIGHT_GRAY.getRGB());
        //this.blit(pPoseStack, relX, relY, 0, 0, this.imageWidth, this.imageHeight);

        for(var widget : backgroundRenderables) {
            widget.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        }
    }
}
