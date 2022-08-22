package com.direwolf20.laserio.common.blockentities;

import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.direwolf20.laserio.common.varia.CustomEnergyStorage;
import com.direwolf20.laserio.setup.Registration;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class PowergenBE extends BlockEntity {

    public static final int POWERGEN_CAPACITY = 50000; // Max capacity
    public static final int POWERGEN_GENERATE = 60;    // Generation per tick
    public static final int POWERGEN_SEND = 200;       // Power to send out per tick

    // Never create lazy optionals in getCapability. Always place them as fields in the tile entity:
    private final ItemStackHandler itemHandler = createHandler();
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

    private final CustomEnergyStorage energyStorage = createEnergy();
    private final LazyOptional<IEnergyStorage> energy = LazyOptional.of(() -> energyStorage);

    private int genTicks;

    public PowergenBE(BlockPos pWorldPosition, BlockState pBlockState) {
        super(Registration.POWERGEN_BE.get(), pWorldPosition, pBlockState);
    }

    public int getGenTicks(){
        return genTicks;
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        handler.invalidate();
        energy.invalidate();
    }

    public void tickServer() {
        if (genTicks > 0) {
            int generated = POWERGEN_GENERATE;
            int willAdd = energyStorage.receiveEnergy(generated, true);
            if (willAdd < generated){

            }
            int added = energyStorage.addEnergy(generated);
            //if (added < generated)
            if (added > 0){
                genTicks--;
                setChanged();
            }
        }

        if (genTicks <= 0) {
            ItemStack stack = itemHandler.getStackInSlot(0);
            int burnTime = ForgeHooks.getBurnTime(stack, RecipeType.SMELTING);
            if (burnTime > 0) {
                itemHandler.extractItem(0, 1, false);
                genTicks = burnTime;
                setChanged();
            }
        }

        BlockState blockState = level.getBlockState(worldPosition);
        if (blockState.getValue(BlockStateProperties.POWERED) != genTicks > 0) {
            level.setBlock(worldPosition, blockState.setValue(BlockStateProperties.POWERED, genTicks > 0),
                    Block.UPDATE_ALL);
        }

        sendOutPower();
    }

    private void sendOutPower() {
        AtomicInteger energy = new AtomicInteger(energyStorage.getEnergyStored());
        if (energy.get() <=0 )
            return;
        for (Direction direction : Direction.values()) {
            BlockEntity be = level.getBlockEntity(worldPosition.relative(direction));
            if (be == null)
                continue;
            boolean doContinue = be.getCapability(CapabilityEnergy.ENERGY, direction.getOpposite()).map(handler -> {
                if (!handler.canReceive())
                    return true;
                int received = handler.receiveEnergy(Math.min(energy.get(), POWERGEN_SEND), false);
                energy.addAndGet(-received);
                energyStorage.consumeEnergy(received);
                return energy.get() > 0;
            }).orElse(true);
            if (!doContinue) {
                return;
            }
        }
    }

    public static final String TagInventory = "Inventory";
    public static final String TagEnergy = "Energy";
    public static final String TagInfo = "Info";
    public static final String TagGenTicks = "GenTicks";

    @Override
    public void load(CompoundTag tag) {
        if (tag.contains(TagInventory)) {
            itemHandler.deserializeNBT(tag.getCompound(TagInventory));
        }
        if (tag.contains(TagEnergy)) {
            energyStorage.deserializeNBT(tag.get(TagEnergy));
        }
        if (tag.contains(TagInfo)) {
            genTicks = tag.getCompound(TagInfo).getInt(TagGenTicks);
        }
        super.load(tag);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        tag.put(TagInventory, itemHandler.serializeNBT());
        tag.put(TagEnergy, energyStorage.serializeNBT());
        CompoundTag infoTag = new CompoundTag();
        infoTag.putInt(TagGenTicks, genTicks);
        tag.put(TagInfo, infoTag);
    }

    @Override
    public CompoundTag getTileData() {
       return super.getTileData();
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(1) {

            @Override
            protected void onContentsChanged(int slot) {
                // To make sure the TE persists when the chunk is saved later we need to
                // mark it dirty every time the item handler changes
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) > 0;
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if (ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) <= 0) {
                    return stack;
                }
                return super.insertItem(slot, stack, simulate);
            }
        };
    }
    
    private CustomEnergyStorage createEnergy() {
        return new CustomEnergyStorage(POWERGEN_CAPACITY, POWERGEN_SEND) {
            @Override
            protected void onEnergyChanged() {
                setChanged();
            }
        };
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }
        if (cap == CapabilityEnergy.ENERGY) {
            return energy.cast();
        }
        return super.getCapability(cap, side);
    }
}
