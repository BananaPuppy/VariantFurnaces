package net.bananapuppy.variantfurnaces.registries.blockentities;

import net.bananapuppy.variantfurnaces.registries.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.recipe.RecipeType;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class EmeraldFurnaceBlockEntity extends AbstractVFurnaceBlockEntity {
    public EmeraldFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.EMERALD_FURNACE, pos, state, RecipeType.SMELTING);
        this.cookTimeTotalSeconds = 2;
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("block.variantfurnaces.emerald_furnace");
    }
}
