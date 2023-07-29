package net.bananapuppy.variantfurnaces.registries.blockentities;

import net.bananapuppy.variantfurnaces.registries.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.recipe.RecipeType;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class IronFurnaceBlockEntity extends AbstractVFurnaceBlockEntity {
    public IronFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.IRON_FURNACE, pos, state, RecipeType.SMELTING);
        this.cookTimeTotalSeconds = 7.0f;
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("block.variantfurnaces.iron_furnace");
    }
}
