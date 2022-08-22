package com.direwolf20.laserio.common.events;

import com.direwolf20.laserio.common.blockentities.PowergenBE;
import com.direwolf20.laserio.setup.Registration;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PickupItemHandler {

    public final static String BlockEntityTag = "BlockEntityTag";

    private static CompoundTag emptyTag;

    public static CompoundTag getEmpty(){
        if (emptyTag == null){
            var defaultState = Registration.POWERGEN.get().defaultBlockState();
            var be = Registration.POWERGEN_BE.get().create(new BlockPos(0, 0, 0), defaultState);
            emptyTag = be.getTileData();
            be.saveAdditional(emptyTag);
        }
        return emptyTag;
    }


    @SubscribeEvent
    public static void pickupItem(EntityItemPickupEvent event) {
        var item = event.getItem();
        var itemStack = item.getItem();
        if (itemStack.getItem() != Registration.POWERGEN_ITEM.get())
            return;
        
        var tag = itemStack.getTagElement(BlockEntityTag);
        //event.getPlayer().displayClientMessage(new TextComponent(tag.getAsString()), false);

        var empty = getEmpty();
        //event.getPlayer().displayClientMessage(new TextComponent(empty.getAsString()), false);

        boolean isEmpty = true;
        for(var key : empty.getAllKeys()){
            if (tag.contains(key) && !tag.get(key).equals(empty.get(key))){
                isEmpty = false;
                break;
            }
        }
        
        if (isEmpty){
            for(var key : tag.getAllKeys()){
                if (!empty.contains(key)){
                    event.getPlayer().displayClientMessage(new TextComponent("Removing: " + tag.get(key).getAsString()), false);
                }
            }
            itemStack.removeTagKey(BlockEntityTag);
        }

        // This happens when you place it in the world (and other times too)
        //         /*var tag = getBlockEntityData(pStack);
        /*if (tag != null){
            
            //empty.getGenTicks() ==0;
        }
        
        //ItemStack itemstack = pItemEntity.getItem();
        //var tag = getBlockEntityData(itemstack);
        if (tag == null)
            return;
        //
        if (tag.contains("Items", 9)) {
            var listtag = tag.getList("Items", 10);
            if (listtag.size() == 0){

            }
            //ItemUtils.onContainerDestroyed(pItemEntity, listtag.stream().map(CompoundTag.class::cast).map(ItemStack::of));
        }*/
        /*boolean hasItems = false;
        for(int i = 0, n = itemHandler.getSlots(); i < n; ++i){
            var stack = itemHandler.getStackInSlot(i);
            if (stack.getCount() > 0)
                hasItems = true;
        }
        if(hasItems)*/
        //else
        //tag.remove(TagInventory);
        /*if (energyStorage.getEnergyStored() > 0)
            tag.put(TagEnergy, energyStorage.serializeNBT());
        else
            tag.remove(TagEnergy);
        if (genTicks > 0){
            CompoundTag infoTag = new CompoundTag();
            infoTag.putInt(TagGenTicks, genTicks);
            tag.put(TagInfo, infoTag);
        } else
            tag.remove(TagInfo);
        if (tag.size() == 0){
            // Remove the tag entirely?
            tag.getAsString();
        }*/
        /*System.out.println("Item picked up! " + itemStack.toString());
        var blockState = event.getState();
        if (blockState.getBlock() == Registration.POWERGEN.get()){
            System.out.println("Powergen broken! " + blockState.toString());
            //event.get
            var defaultState = Registration.POWERGEN.get().defaultBlockState();
            // Check if 'empty'
            //var empty = Registration.POWERGEN_BE.get().create(blockState., defaultState);
            
        }*/
    }
}
