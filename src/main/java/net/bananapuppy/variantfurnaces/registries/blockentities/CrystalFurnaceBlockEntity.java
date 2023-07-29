package net.bananapuppy.variantfurnaces.registries.blockentities;

import net.bananapuppy.variantfurnaces.registries.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.recipe.RecipeType;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class CrystalFurnaceBlockEntity extends AbstractVFurnaceBlockEntity {
    public CrystalFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CRYSTAL_FURNACE, pos, state, RecipeType.SMELTING);
        this.cookTimeTotalSeconds = 2.0f;
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("block.variantfurnaces.crystal_furnace");
    }
}
