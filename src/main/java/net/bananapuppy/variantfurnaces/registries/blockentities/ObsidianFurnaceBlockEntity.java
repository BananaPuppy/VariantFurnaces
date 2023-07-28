package net.bananapuppy.variantfurnaces.registries.blockentities;

import net.bananapuppy.variantfurnaces.registries.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.recipe.RecipeType;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class ObsidianFurnaceBlockEntity extends AbstractVFurnaceBlockEntity {
    public ObsidianFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.OBSIDIAN_FURNACE, pos, state, RecipeType.SMELTING);
        this.cookTimeTotalSeconds = 1;
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("block.variantfurnaces.obsidian_furnace");
    }
}
