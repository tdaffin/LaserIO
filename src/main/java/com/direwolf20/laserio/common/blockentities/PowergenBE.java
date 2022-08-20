package com.direwolf20.laserio.common.blockentities;

import com.direwolf20.laserio.setup.Registration;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class PowergenBE extends BlockEntity {

    public PowergenBE(BlockPos pWorldPosition, BlockState pBlockState) {
        super(Registration.POWERGEN_BE.get(), pWorldPosition, pBlockState);
    }
    
}
