package com.direwolf20.laserio.common.containers;

import com.direwolf20.laserio.setup.Registration;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class PowergenContainer extends AbstractContainerMenu {

    public PowergenContainer(int windowId, BlockPos readBlockPos, Inventory inv, Player player) {
        super(Registration.POWERGEN_CONTAINER.get(), windowId);
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return false;
    }
    
}
