package net.bananapuppy.variantfurnaces.registries.screens;

import net.bananapuppy.variantfurnaces.registries.ModItems;
import net.bananapuppy.variantfurnaces.registries.ModScreenHandlers;
import net.bananapuppy.variantfurnaces.registries.blockentities.AbstractVFurnaceBlockEntity;
import net.bananapuppy.variantfurnaces.registries.slots.VFurnaceAugmentSlot;
import net.bananapuppy.variantfurnaces.registries.slots.VFurnaceFuelSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.FurnaceOutputSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VFurnaceScreenHandler extends ScreenHandler {
    public VFurnaceScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(AbstractVFurnaceBlockEntity.inventorySize), new ArrayPropertyDelegate(AbstractVFurnaceBlockEntity.PROPERTY_COUNT), RecipeType.SMELTING, null);
    }
    public VFurnaceScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate delegate, RecipeType<? extends AbstractCookingRecipe> mainRecipeType, @Nullable RecipeType<? extends AbstractCookingRecipe> secondaryRecipeType) {
        super(ModScreenHandlers.VFURNACE_SCREEN_HANDLER, syncId);

        checkSize(inventory, AbstractVFurnaceBlockEntity.inventorySize);

        this.inventory = inventory;
        inventory.onOpen(playerInventory.player);
        this.propertyDelegate = delegate;

        //Furnace Slots
        this.addSlot(new Slot(inventory, 0, 56, 17));
        this.addSlot(new VFurnaceFuelSlot(this, inventory, 1, 56, 53));
        this.addSlot(new FurnaceOutputSlot(playerInventory.player, inventory, 2, 116, 35));
        //Upgrade Slots
        this.addSlot(new VFurnaceAugmentSlot(this, inventory, 3, 147, 8));
        this.addSlot(new VFurnaceAugmentSlot(this, inventory, 4, 147, 26));
        this.addSlot(new VFurnaceAugmentSlot(this, inventory, 5, 147, 44));
        this.addSlot(new VFurnaceAugmentSlot(this, inventory, 6, 147, 62));
        //BaseVFurnaceBlockEntity.inventorySize = lastindex+1

        this.world = playerInventory.player.getWorld();
        this.mainRecipeType = mainRecipeType;
        this.secondaryRecipeType = secondaryRecipeType;

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        addProperties(delegate);
    }

    private final World world;
    @SuppressWarnings("FieldMayBeFinal")
    private RecipeType<? extends AbstractCookingRecipe> mainRecipeType;
    @SuppressWarnings("FieldMayBeFinal")
    private RecipeType<? extends AbstractCookingRecipe> secondaryRecipeType;

    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;

    private List<Item> getAugments(){
        return List.of(
                ModItems.FUEL_AUGMENT,
                ModItems.SPEED_AUGMENT,
                ModItems.BLASTING_AUGMENT,
                ModItems.SMOKE_AUGMENT
        );
    }
    private List<Item> getAugmentSlotItems(){
        return List.of(
                inventory.getStack(3).getItem(),
                inventory.getStack(4).getItem(),
                inventory.getStack(5).getItem(),
                inventory.getStack(6).getItem()
        );
    }
    @SuppressWarnings("RedundantIfStatement")
    public boolean isAugmentAndNotSloted(ItemStack itemStack) {
        Item item = itemStack.getItem();
        if(getAugments().contains(item)){
            if(!getAugmentSlotItems().contains(item)){
                return true;
            }
        }
        return false;
    }

    public boolean isFuel(ItemStack itemStack) {
        return AbstractVFurnaceBlockEntity.canUseAsFuel(itemStack);
    }
    @SuppressWarnings("RedundantIfStatement")
    private boolean isSmeltable(ItemStack itemStack) {
        if(this.world.getRecipeManager().getFirstMatch(this.mainRecipeType, new SimpleInventory(itemStack), this.world).isPresent()){
            return true;
        }
        if(this.world.getRecipeManager().getFirstMatch(this.secondaryRecipeType, new SimpleInventory(itemStack), this.world).isPresent()){
            return true;
        }
        return false;
    }

    public int getFuelProgress() {
        int fuelTime = this.propertyDelegate.get(AbstractVFurnaceBlockEntity.FUEL_TIME_PROPERTY_INDEX);
        if (fuelTime == 0) {
            fuelTime = (int)AbstractVFurnaceBlockEntity.DEFAULT_COOK_TIME;
        }
        return this.propertyDelegate.get(AbstractVFurnaceBlockEntity.BURN_TIME_PROPERTY_INDEX) * 13 / fuelTime;
    }

    public int getCookProgress() {
        int cookTime = this.propertyDelegate.get(AbstractVFurnaceBlockEntity.COOK_TIME_PROPERTY_INDEX);
        int cookTimeTotal = this.propertyDelegate.get(AbstractVFurnaceBlockEntity.COOK_TIME_TOTAL_PROPERTY_INDEX);
        if (cookTimeTotal == 0 || cookTime == 0) {
            return 0;
        }
        return cookTime * 24 / cookTimeTotal;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slotIndex) { //Shift-Click Stack Behavior
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);

        int furnaceSlotCount = 3 + 4;
        @SuppressWarnings("UnnecessaryLocalVariable")
        int startIndexInv = furnaceSlotCount;
        int endIndexInv = 27 + furnaceSlotCount;
        int endIndexHotbar = 36 + furnaceSlotCount;

        if (slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            //Taking
            if (slotIndex == 2) { //Cooked
                if (!this.insertItem(itemStack2, startIndexInv, endIndexHotbar, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickTransfer(itemStack2, itemStack);
            }
            if(slotIndex >= 0 && slotIndex <= 1) { //Fuel&Cooking
                if(!this.insertItem(itemStack2, startIndexInv, endIndexHotbar, false)){
                    return ItemStack.EMPTY;
                }
                slot.onQuickTransfer(itemStack2, itemStack);
            }
            if(slotIndex >= 3 && slotIndex <= 6) { //Augments
                if(!this.insertItem(itemStack2, startIndexInv, endIndexHotbar, false)){
                    return ItemStack.EMPTY;
                }
                slot.onQuickTransfer(itemStack2, itemStack);
            }
            //Inserting
            if(this.isSmeltable(itemStack2)){ //Cooking
                if(!this.insertItem(itemStack2, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickTransfer(itemStack2, itemStack);
            }
            if(this.isFuel(itemStack2)){ //Fuel
                if(!this.insertItem(itemStack2, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickTransfer(itemStack2, itemStack);
            }
            if(this.isAugmentAndNotSloted(itemStack2)){ //Augments
                if(!this.insertItemSingularFromStack(slotIndex, itemStack2, 3, furnaceSlotCount, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickTransfer(itemStack2, itemStack);
            }
            //Non-Furnace Inv Management
            if(slotIndex >= 3+4 && slotIndex < 30+4){ //From Inv
                if(!this.insertItem(itemStack2, endIndexInv, endIndexHotbar, false)){
                    return ItemStack.EMPTY;
                }
            }
            if(slotIndex >= 30+4 && slotIndex < 39+4){ //From Hotbar
                if(!this.insertItem(itemStack2, startIndexInv, endIndexInv, false)){
                    return ItemStack.EMPTY;
                }
            }

            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTakeItem(player, itemStack2);
        }
        return itemStack;
    }

    @SuppressWarnings("SameParameterValue")
    protected boolean insertItemSingularFromStack(int fromSlotIndex,ItemStack stack, int startIndex, int endIndex, boolean fromLast) {
        ItemStack itemStack;
        Slot slot;
        boolean bl = false;
        int i = startIndex;
        if (fromLast) {
            i = endIndex - 1;
        }
//        if (stack.isStackable()) {
//            while (!stack.isEmpty() && (fromLast ? i >= startIndex : i < endIndex)) {
//                slot = this.slots.get(i);
//                itemStack = slot.getStack();
//                if (!itemStack.isEmpty() && ItemStack.canCombine(stack, itemStack)) {
//                    int j = itemStack.getCount() + stack.getCount();
//                    if (j <= stack.getMaxCount()) {
//                        stack.setCount(0);
//                        itemStack.setCount(j);
//                        slot.markDirty();
//                        bl = true;
//                    } else if (itemStack.getCount() < stack.getMaxCount()) {
//                        stack.decrement(stack.getMaxCount() - itemStack.getCount());
//                        itemStack.setCount(stack.getMaxCount());
//                        slot.markDirty();
//                        bl = true;
//                    }
//                }
//                if (fromLast) {
//                    --i;
//                    continue;
//                }
//                ++i;
//            }
//        }
        if (!stack.isEmpty()) {
            while (fromLast ? i >= startIndex : i < endIndex) {
                slot = this.slots.get(i);
                itemStack = slot.getStack();
                if (itemStack.isEmpty() && slot.canInsert(stack)) {
                    if (stack.getCount() > slot.getMaxItemCount()) {
                        slot.setStack(stack.split(slot.getMaxItemCount()));
                    } else {
                        ItemStack stackTo = stack.copy();
                        stackTo.setCount(1);
                        slot.setStack(stackTo);

                        Slot slotFrom = this.slots.get(fromSlotIndex);
                        ItemStack stackFrom = stack.copy();
                        stackFrom.setCount(stack.getCount()-1);
                        slotFrom.setStack(stackFrom);

                        stack.setCount(0);
                    }
                    slot.markDirty();
                    bl = true;
                    break;
                }
                if (fromLast) {
                    --i;
                    continue;
                }
                ++i;
            }
        }
        return bl;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    private void addPlayerInventory(PlayerInventory playerInventory){
        for(int i = 0; i < 3; i++){
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + (j * 18), 84 + (i * 18)));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory){
        for(int i = 0; i < 9; i++){
            this.addSlot(new Slot(playerInventory, i, 8 + (i * 18), 142));
        }
    }

    public boolean isBurning(){
        return propertyDelegate.get(AbstractVFurnaceBlockEntity.BURN_TIME_PROPERTY_INDEX) > 0;
    }

}
