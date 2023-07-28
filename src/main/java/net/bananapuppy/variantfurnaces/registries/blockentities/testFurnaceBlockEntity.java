package net.bananapuppy.variantfurnaces.registries.blockentities;

import net.bananapuppy.variantfurnaces.Config;
import net.bananapuppy.variantfurnaces.registries.ModBlockEntities;
import net.bananapuppy.variantfurnaces.registries.screens.VFurnaceScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class testFurnaceBlockEntity extends BaseVFurnaceBlockEntity{
    public testFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TEST_FURNACE, pos, state, RecipeType.SMELTING);
        this.cookTimeTotalSeconds = 1;
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("container.variantfurnaces.testfurnace");
    }

    @Override
    protected int getFuelTime(ItemStack fuel) {
        if(Config.fuelScalesWithSpeed){
            return (int)((float)(super.getFuelTime(fuel) / 10) * this.cookTimeTotalSeconds);
        }
        return super.getFuelTime(fuel);
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        RecipeType<? extends AbstractCookingRecipe> mainType = RecipeType.SMELTING;
        RecipeType<? extends AbstractCookingRecipe> secondType = null;

        boolean isMainSet = false;
        if(isBlastingAugmented()){
            isMainSet = true;
            mainType = RecipeType.BLASTING;
        }
        if(isSmokeAugmented()){
            if(isMainSet){
                secondType = RecipeType.SMOKING;
            } else {
                secondType = RecipeType.SMOKING;
            }
        }
        return new VFurnaceScreenHandler(syncId, playerInventory, this, this.propertyDelegate, mainType, secondType);
    }
}
