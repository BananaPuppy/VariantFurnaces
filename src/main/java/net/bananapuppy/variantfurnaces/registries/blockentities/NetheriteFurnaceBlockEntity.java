package net.bananapuppy.variantfurnaces.registries.blockentities;

import net.bananapuppy.variantfurnaces.registries.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.recipe.RecipeType;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class NetheriteFurnaceBlockEntity extends AbstractVFurnaceBlockEntity {
    public NetheriteFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.NETHERITE_FURNACE, pos, state, RecipeType.SMELTING);
        this.cookTimeTotalSeconds = 1.0f/4.0f;
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("block.variantfurnaces.netherite_furnace");
    }
}
