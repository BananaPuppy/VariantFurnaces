package net.bananapuppy.variantfurnaces.registries.slots;

import net.bananapuppy.variantfurnaces.registries.screens.VFurnaceScreenHandler;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class VFurnaceAugmentSlot extends Slot {
    private final VFurnaceScreenHandler handler;

    public VFurnaceAugmentSlot(VFurnaceScreenHandler handler, Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        this.handler = handler;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return (this.isAugmentAndNotAlready(stack));
    }
    
    public boolean isAugmentAndNotAlready(ItemStack itemStack) {
        return this.handler.isAugmentAndNotSloted(itemStack);
    }

    @Override
    public int getMaxItemCount(ItemStack stack) {
        return 1;
    }
}
