package com.direwolf20.laserio.client.screens;

import com.direwolf20.laserio.client.renderer.RenderUtils;
import com.direwolf20.laserio.client.screens.widgets.CardHeaderPanel;
import com.direwolf20.laserio.client.screens.widgets.Panel;
import com.direwolf20.laserio.client.screens.widgets.SidePanel;
import com.direwolf20.laserio.common.LaserIO;
import com.direwolf20.laserio.common.blocks.LaserNode;
import com.direwolf20.laserio.common.containers.AbstractCardContainer;
import com.direwolf20.laserio.common.items.cards.BaseCard;
import com.direwolf20.laserio.util.MiscTools;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractCardScreen<T extends AbstractCardContainer> extends AbstractContainerScreen<T>  {

    public final static String ReturnButton = "return";
    public final static String SideButton = "side";
    public final static String HeaderPanel = "header";
    
    public final BaseCard.CardType CardType;

    private final ResourceLocation GUI = new ResourceLocation(LaserIO.MODID, "textures/gui/redstonecard.png");

    private final int HeaderOffset = 20 - 3;
    private final int LaserWidth = 4;
    private final int LaserInset = 5;
    private final int ColorInset = 5;

    protected final T baseContainer;
    protected final ItemStack card;

    protected Map<String, Button> buttons = new HashMap<>();
    public final Map<String, AbstractWidget> widgets = new HashMap<>();
    public final List<AbstractWidget> backgroundRenderables = new ArrayList<>();
    private String sideName;

    public AbstractCardScreen(T container, Inventory pPlayerInventory, Component pTitle) {
        super(container, pPlayerInventory, pTitle);
        this.baseContainer = container;
        card = container.cardItem;
        Item cardItem = card.getItem();
        BaseCard baseCard = cardItem instanceof BaseCard ? (BaseCard) cardItem : null;
        CardType = baseCard != null ? baseCard.getCardType() : BaseCard.CardType.MISSING;
    }

    // TODO: Use a map on CardType
    public abstract Component cardTypeName();

    public abstract void openNode();

    public void addCommonWidgets() {
        Color color = RenderUtils.getColor(CardType);
        //CardHeaderPanel cardHeaderPanel = new CardHeaderPanel(getGuiLeft() + 4, getGuiTop() + 4, 74, 18, this.cardTypeName(), color);
        int outset = 5;
        //int height = imageHeight + outset * 2;
        int height = 85;
        CardHeaderPanel cardHeaderPanel = new CardHeaderPanel(getGuiLeft() - outset, getGuiTop() - outset, imageWidth + outset*2, height, this.cardTypeName(), color);
        backgroundRenderables.add(cardHeaderPanel);

        //buttons.put(CardTypeButton, new Button(getGuiLeft(), getGuiTop() - HeaderOffset, 80, 20, this.cardTypeName(), (button) -> {}));
        if (baseContainer.direction == -1)
            return;
        var returnText = new TextComponent("<--");
        buttons.put(ReturnButton, new Button(getGuiLeft() - 25, getGuiTop() + 1, 25, 20, returnText, (button) -> {
            openNode();
        }));
        // NOTE: Return button -- perhaps put indication of direction and what is there near here?

        var sideCmp = LaserNodeScreen.sides[baseContainer.direction];
        sideName = sideCmp.getString();
        ItemStack blockItemStack = null;
        var stateFaced = baseContainer.getBlockStateFaced();
        if (stateFaced != null){
            var blockFaced = stateFaced.getBlock();
            if (blockFaced != null){
                sideName += ": " + blockFaced.getName().getString();
                var blockItem = blockFaced.asItem();
                if (blockItem != null)
                    blockItemStack = new ItemStack(blockItem);
            }
        }
        var sideText = sideCmp;
        int x = getGuiLeft() - 37, y = getGuiTop() + 26;
        int w = 40, h = 40;
        var sideWidget = new SidePanel(x, y, w, h, sideText, color, blockItemStack, this.itemRenderer);
        widgets.put(SideButton, sideWidget);
        addRenderableOnly(sideWidget);
    }

    protected void renderTooltip(PoseStack pPoseStack, int mouseX, int mouseY) {
        super.renderTooltip(pPoseStack, mouseX, mouseY);
        Button returnButton = buttons.get(ReturnButton);
        if (MiscTools.inBounds(returnButton, mouseX, mouseY)) {
            ArrayList<Component> tooltips = new ArrayList<Component>();
            tooltips.add(new TranslatableComponent(LaserNode.SCREEN_LASERNODE));
            //tooltips.add(LaserNodeScreen.sides[baseContainer.direction]);
            this.renderComponentTooltip(pPoseStack, tooltips, mouseX, mouseY);
        }
        AbstractWidget sideWidget = widgets.get(SideButton);
        if (MiscTools.inBounds(sideWidget, mouseX, mouseY)) {
            ArrayList<Component> tooltips = new ArrayList<Component>();
            //tooltips.add(LaserNodeScreen.sides[baseContainer.direction]);
            //tooltips.add(sideWidget.getMessage());
            tooltips.add(new TextComponent(sideName));
            this.renderComponentTooltip(pPoseStack, tooltips, mouseX, mouseY);
        }
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);   
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        for(Widget widget : backgroundRenderables) {
            widget.render(matrixStack, mouseX, mouseY, partialTicks);
        }

/*
        Color color = RenderUtils.getColor(CardType);
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;

        Component text = this.cardTypeName();
        int textWidth = font.width(text);

        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        //int x = relX + 0;
        //int y = relY + 4;
        //int w = this.imageWidth;
        //int h = 11;
        //fill(matrixStack, x + 2, y + 2, x + w, y + h + 2, 0xFFC6C6C6);
        //fill(matrixStack, x, y + h, x + 2, y + h + 1, 0xFFFFFFFF);
        //fill(matrixStack, x + w, y + h, x + w + 2, y + h + 1, 0xFF868686);
        int laserY = relY - HeaderOffset/2;
        int hh = LaserWidth/2;
        fill(matrixStack, relX + LaserInset, laserY - hh, relX + this.imageWidth - LaserInset, laserY + hh, color.getRGB());

        int x = getGuiLeft();
        int y = getGuiTop() - HeaderOffset;


        
        int width = textWidth + 8;//80;
        int height = 20;

        float alpha = 1.0F;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, AbstractWidget.WIDGETS_LOCATION);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
        int i = 1;
        int vOffset = 46 + i * 20;
        //RenderSystem.enableBlend();
        //RenderSystem.defaultBlendFunc();
        //RenderSystem.enableDepthTest();
        this.blit(matrixStack, x, y, 0, vOffset, width / 2, height);
        this.blit(matrixStack, x + width / 2, y, 200 - width / 2, vOffset, width / 2, height);
        //this.renderBg(matrixStack, minecraft, pMouseX, pMouseY);
        //int j = getFGColor();
        int j = Color.WHITE.getRGB();
        drawCenteredString(matrixStack, font, text, x + width / 2, y + (height - 8) / 2, j | Mth.ceil(alpha * 255.0F) << 24);
*/
        //FormattedCharSequence chars = text.getVisualOrderText();
        //float x = relX + this.imageWidth / 2f - font.width(text) / 2f;
        //float y = relY - HeaderOffset + ColorInset + 4;
        //font.drawShadow(matrixStack, text, x, y, Color.WHITE.getRGB());

        /*RenderSystem.setShaderTexture(0, GUI);
        this.blit(matrixStack, relX, relY - HeaderOffset, 0, 0, this.imageWidth, this.imageHeight);
        fill(matrixStack, relX + ColorInset, relY - HeaderOffset + ColorInset,
            relX + this.imageWidth - ColorInset, relY, color.getRGB());

        Font font = Minecraft.getInstance().font;
        matrixStack.pushPose();
        float scale = 1f;
        matrixStack.scale(scale, scale, scale);
        FormattedCharSequence text = this.cardTypeName().getVisualOrderText();
        float x = (relX + this.imageWidth / 2f - font.width(text) / 2f) / scale;
        float y = (relY - HeaderOffset + ColorInset + 4) / scale;
        font.drawShadow(matrixStack, text, x, y, Color.LIGHT_GRAY.getRGB());
        matrixStack.popPose();*/

        
    }
    
}
