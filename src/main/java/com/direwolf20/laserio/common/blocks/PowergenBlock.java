package com.direwolf20.laserio.common.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

public class PowergenBlock extends Block {

    public PowergenBlock() {
        super(Properties.of(Material.METAL)
            .sound(SoundType.METAL)
            //.lightLevel
        );
    }
    
    public PowergenBlock(Properties p_49795_) {
        super(p_49795_);
    }


    
}
