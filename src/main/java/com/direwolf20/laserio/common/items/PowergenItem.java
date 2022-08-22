package com.direwolf20.laserio.common.items;

import java.util.List;

import javax.annotation.Nullable;

import com.direwolf20.laserio.common.blockentities.PowergenBE;
import com.direwolf20.laserio.setup.Registration;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class PowergenItem extends BlockItem {

    public PowergenItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
        this.getBlock().appendHoverText(pStack, pLevel, pTooltip, pFlag);
        var tag = getBlockEntityData(pStack);
        if (tag == null)
            return;
        if (tag.contains(PowergenBE.TagInventory)) {
            var itemList = tag.getCompound(PowergenBE.TagInventory).getList("Items", Tag.TAG_COMPOUND);
            var item = itemList.getCompound(0);
            var itemStack = ItemStack.of(item);
            if (itemStack.getCount() > 0){
                pTooltip.add(new TextComponent("Items: ")
                    .append("" + itemStack.getCount() + " ")    
                    .append(itemStack.getHoverName())    
                    .withStyle(ChatFormatting.GREEN));
            }
        }
        if (tag.contains(PowergenBE.TagEnergy)) {
            var tEnergy = tag.get(PowergenBE.TagEnergy);
            if (tEnergy instanceof IntTag energy)
                pTooltip.add(new TextComponent("Energy: " + energy)
                    .withStyle(ChatFormatting.YELLOW));
        }
        if (tag.contains(PowergenBE.TagInfo)) {
            var genTicks = tag.getCompound(PowergenBE.TagInfo).getInt(PowergenBE.TagGenTicks);
            pTooltip.add(new TextComponent("Generating: " + genTicks)
                    .withStyle(ChatFormatting.YELLOW));
        }
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pPos, Level pLevel, @Nullable Player pPlayer, ItemStack pStack, BlockState pState) {
        // Called when put block down
        return super.updateCustomBlockEntityTag(pLevel, pPlayer, pPos, pStack);
    }

    @Override
    public void onDestroyed(ItemEntity pItemEntity) {
        // Not getting called
        ItemStack itemstack = pItemEntity.getItem();
        var tag = getBlockEntityData(itemstack);
        if (tag == null)
            return;
        //
        if (tag.contains("Items", 9)) {
            var listtag = tag.getList("Items", 10);
            if (listtag.size() == 0){

            }
            //ItemUtils.onContainerDestroyed(pItemEntity, listtag.stream().map(CompoundTag.class::cast).map(ItemStack::of));
        }

     }
    
}
