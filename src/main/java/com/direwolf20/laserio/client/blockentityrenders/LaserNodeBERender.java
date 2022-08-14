package com.direwolf20.laserio.client.blockentityrenders;

import com.direwolf20.laserio.client.blockentityrenders.baseberender.BaseLaserBERender;
import com.direwolf20.laserio.client.renderer.DelayedRenderer;
import com.direwolf20.laserio.common.blockentities.LaserNodeBE;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

import java.awt.*;

public class LaserNodeBERender extends BaseLaserBERender<LaserNodeBE> {
    public static final Vector3f[] offsets = {
            new Vector3f(0.65f, 0.65f, 0.5f),
            new Vector3f(0.5f, 0.65f, 0.5f),
            new Vector3f(0.35f, 0.65f, 0.5f),
            new Vector3f(0.65f, 0.5f, 0.5f),
            new Vector3f(0.5f, 0.5f, 0.5f),
            new Vector3f(0.35f, 0.5f, 0.5f),
            new Vector3f(0.65f, 0.35f, 0.5f),
            new Vector3f(0.5f, 0.35f, 0.5f),
            new Vector3f(0.35f, 0.35f, 0.5f)
    };
    public static final Color colors[] = {
            new Color(255, 255, 255),
            new Color(249, 128, 29),
            new Color(198, 79, 189),
            new Color(58, 179, 218),
            new Color(255, 216, 61),
            new Color(128, 199, 31),
            new Color(243, 140, 170),
            new Color(71, 79, 82),
            new Color(156, 157, 151),
            new Color(22, 156, 157),
            new Color(137, 50, 183),
            new Color(60, 68, 169),
            new Color(130, 84, 50),
            new Color(93, 124, 21),
            new Color(176, 46, 38),
            new Color(29, 28, 33)
    };
    /*{
        new Color(0xf9ffff),    // White
        new Color(0xf9801d),    // Orange
        new Color(0xc64fbd),    // Magenta
        new Color(0x3ab3da),    // Cyan
        new Color(0xffd83d),    // Yellow -- Energy
        new Color(0x80c71f),    // Green -- Item
        new Color(0xf38caa),    // Pink
        new Color(0x474f52),    // DarkGrey
        new Color(0x9c9d97),    // LightGrey
        new Color(0x169c9d),    // DarkCyan
        new Color(0x8932b7),    // Purple
        new Color(0x3c44a9),    // DarkBlue -- Fluid
        new Color(0x825432),    // Brown
        new Color(0x5d7c15),    // DarkGreen
        new Color(0xb02e26),    // * Red
        new Color(0x1d1c21),    // * Black
    };*/


    public LaserNodeBERender(BlockEntityRendererProvider.Context context) {
        super(context);

    }

    @Override
    public void render(LaserNodeBE blockentity, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightsIn, int combinedOverlayIn) {
        super.render(blockentity, partialTicks, matrixStackIn, bufferIn, combinedLightsIn, combinedOverlayIn);
        if (!blockentity.rendersChecked)
            blockentity.populateRenderList();
        DelayedRenderer.addConnecting(blockentity);
    }
}
