package com.direwolf20.laserio.common.varia;

import net.minecraft.nbt.Tag;
import net.minecraftforge.energy.EnergyStorage;

public class CustomEnergyStorage extends EnergyStorage {

    public CustomEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    protected void onEnergyChanged() {
        //setChanged();
    }

    @Override
    public int getEnergyStored()
    {
        return energy;
    }

    public void setEnergy(int energy) {
        int startEnergy = getEnergyStored();
        extractEnergy(startEnergy, false);
        receiveEnergy(energy, false);
        if (getEnergyStored() != startEnergy)
            onEnergyChanged();
    }

    public int addEnergy(int energy) {
        int added = receiveEnergy(energy, false);
        if (added != 0)
            onEnergyChanged();
        return added;
    }

    public int consumeEnergy(int energy) {
        int consumed = extractEnergy(energy, false);
        if (consumed != 0)
            onEnergyChanged();
        return consumed;
    }

    @Override
    public void deserializeNBT(Tag nbt)
    {
        int startEnergy = getEnergyStored();
        super.deserializeNBT(nbt);
        if (getEnergyStored() != startEnergy)
            onEnergyChanged();
    }

}
