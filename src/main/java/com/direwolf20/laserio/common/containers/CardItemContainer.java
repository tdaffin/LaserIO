package com.direwolf20.laserio.common.containers;

import com.direwolf20.laserio.common.blockentities.LaserNodeBE;
import com.direwolf20.laserio.common.containers.customhandler.CardItemHandler;
import com.direwolf20.laserio.common.containers.customhandler.FilterBasicHandler;
import com.direwolf20.laserio.common.containers.customslot.CardItemSlot;
import com.direwolf20.laserio.common.containers.customslot.FilterBasicSlot;
import com.direwolf20.laserio.common.items.cards.BaseCard;
import com.direwolf20.laserio.common.items.filters.BaseFilter;
import com.direwolf20.laserio.common.items.filters.FilterBasic;
import com.direwolf20.laserio.common.items.filters.FilterCount;
import com.direwolf20.laserio.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class CardItemContainer extends AbstractContainerMenu {
    public static final int SLOTS = 2;
    public static final int FILTERSLOTS = 15;
    public CardItemHandler handler;
    public FilterBasicHandler filterHandler;
    public ItemStack cardItem;
    public Player playerEntity;
    private IItemHandler playerInventory;
    public BlockPos sourceContainer = BlockPos.ZERO;

    public CardItemContainer(int windowId, Inventory playerInventory, Player player, FriendlyByteBuf extraData) {
        this(windowId, playerInventory, player, new CardItemHandler(SLOTS, ItemStack.EMPTY), ItemStack.EMPTY);
        cardItem = extraData.readItem();
    }

    public CardItemContainer(int windowId, Inventory playerInventory, Player player, CardItemHandler handler, ItemStack cardItem) {
        super(Registration.CardItem_Container.get(), windowId);
        playerEntity = player;
        this.handler = handler;
        this.playerInventory = new InvWrapper(playerInventory);
        this.cardItem = cardItem;
        if (handler != null) {
            addSlotRange(handler, 0, 80, 5, 1, 18);
            addSlotRange(handler, 1, 153, 5, 1, 18);
            getFilterHandler();
            addSlotBox(filterHandler, 0, 44, 25, 5, 18, 3, 18);
            toggleFilterSlots();
        }

        layoutPlayerInventorySlots(8, 84);
        //toggleFilterSlots(true);
    }

    @Override
    public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player) {
        if (slotId >= SLOTS && slotId < SLOTS + FILTERSLOTS) {
            return;
        }
        super.clicked(slotId, dragType, clickTypeIn, player);
    }

    public CardItemContainer(int windowId, Inventory playerInventory, Player player, CardItemHandler handler, BlockPos sourcePos, ItemStack cardItem) {
        this(windowId, playerInventory, player, handler, cardItem);
        this.sourceContainer = sourcePos;
    }

    public int getStackSize(int slot) {
        ItemStack filterStack = slots.get(0).getItem();
        if (slot >= SLOTS && slot < SLOTS + FILTERSLOTS && (slots.get(slot) instanceof FilterBasicSlot) && filterStack.getItem() instanceof FilterCount) {
            CompoundTag compound = filterStack.getOrCreateTag();
            ListTag countList = compound.getList("counts", Tag.TAG_COMPOUND);
            for (int i = 0; i < countList.size(); i++) {
                CompoundTag countTag = countList.getCompound(i);
                int tagslot = countTag.getInt("Slot");
                if (tagslot == slot - SLOTS)
                    return countTag.getInt("Count");
            }
        }
        return 0;
    }

    public void getFilterHandler() {
        ItemStack filterStack = slots.get(0).getItem(); //BaseCard.getInventory(cardItem).getStackInSlot(0);
        if (filterStack.getItem() instanceof FilterBasic)
            filterHandler = FilterBasic.getInventory(filterStack);
        else if (filterStack.getItem() instanceof FilterCount)
            filterHandler = FilterCount.getInventory(filterStack);
        else
            filterHandler = new FilterBasicHandler(15, ItemStack.EMPTY);
    }

    public void toggleFilterSlots() {
        getFilterHandler();
        updateFilterSlots(filterHandler, 0, 44, 25, 5, 18, 3, 18);
    }

    @Override
    public boolean stillValid(Player playerIn) {
        if (sourceContainer.equals(BlockPos.ZERO))
            return playerIn.getMainHandItem().equals(cardItem) || playerIn.getOffhandItem().equals(cardItem);
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            itemstack = stack.copy();
            //If its one of the 3 slots at the top try to move it into your inventory
            if (index < SLOTS) {
                if (!this.moveItemStackTo(stack, SLOTS, 36 + SLOTS, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(stack, itemstack);
            } else if (index >= SLOTS && index < SLOTS + FILTERSLOTS) {
                //No-Op
            } else { //From player inventory TO something
                ItemStack currentStack = slot.getItem().copy();
                if (slots.get(0).mayPlace(currentStack) || slots.get(1).mayPlace(currentStack)) {
                    if (!this.moveItemStackTo(stack, 0, SLOTS, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slots.get(0).getItem().getItem() instanceof BaseFilter) {
                    if (!(slots.get(0).getItem().getItem() instanceof FilterCount))
                        currentStack.setCount(1);
                    for (int i = SLOTS; i < SLOTS + FILTERSLOTS; i++) { //Prevents the same item from going in there more than once.
                        if (this.slots.get(i).getItem().equals(currentStack, false)) //Don't limit tags
                            return ItemStack.EMPTY;
                    }
                    if (!this.moveItemStackTo(currentStack, SLOTS, SLOTS + FILTERSLOTS, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                if (!playerIn.level.isClientSide())
                    BaseCard.setInventory(cardItem, handler);
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, stack);
        }

        return itemstack;
    }

    private void updateFilterSlots(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            for (int i = 0; i < horAmount; i++) {
                if (handler instanceof CardItemHandler && index == 0)
                    System.out.println("This shouldn't happen");
                else if (handler instanceof FilterBasicHandler) {
                    slots.set(index + SLOTS, new FilterBasicSlot(handler, index, x, y, slots.get(0).getItem().getItem() instanceof FilterCount));
                    slots.get(index + SLOTS).index = index + SLOTS; //Look at container.addSlot() -- it does this
                } else
                    System.out.println("This shouldn't happen");
                x += dx;
                index++;
            }
            y += dy;
            x = x - (dx * horAmount);
        }
    }

    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            if (handler instanceof CardItemHandler && index == 0)
                addSlot(new CardItemSlot(handler, this, index, x, y));
            else if (handler instanceof FilterBasicHandler)
                addSlot(new FilterBasicSlot(handler, index, x, y, slots.get(0).getItem().getItem() instanceof FilterCount));
            else
                addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    private void layoutPlayerInventorySlots(int leftCol, int topRow) {
        // Player inventory
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }

    @Override
    public void removed(Player playerIn) { //TODO This fires when we open the filter container from the item container
        Level world = playerIn.getLevel();
        if (!world.isClientSide) {
            BaseCard.setInventory(cardItem, handler);
            if (!sourceContainer.equals(BlockPos.ZERO)) {
                BlockEntity blockEntity = world.getBlockEntity(sourceContainer);
                if (blockEntity instanceof LaserNodeBE)
                    ((LaserNodeBE) blockEntity).updateThisNode();
            }
        }
        super.removed(playerIn);
    }
}
